#include "geminc.h"
#include "gemprm.h"
#include "pgprm.h"
#include "vgstruct.h"

/*
 *---------------------------------------------------------------------*
 *          Functions used by vgfToXml from other libraries            *
 *---------------------------------------------------------------------*
 */
void cvg_openj ( char *filnam, int wrtflg, FILE **fptr, int *iret );
void cvg_rdjrecnoc ( char *fname, FILE *fptr, int fpos, VG_DBStruct *el, int *iret );
int  cvg_v2x ( VG_DBStruct *el, char *buffer, int *iret );
void cgr_segdist ( int *np, float *xx, float *yy, float *fx, float *fy,
     float *distance, int *nearest_vrt, int *next_vrt, float *nx, float *ny, int *iret );


/*
 *---------------------------------------------------------------------*
 *          Private functions and macros defined by vgfToXml              *
 *---------------------------------------------------------------------*
 */ 
 
struct list_el {
   VG_DBStruct el;
   struct list_el * next;
};
typedef struct list_el item1;

void insert(item1 ** head, int sd);

item1 *llist_bubble_sort(item1 *curr2);
item1 *llist_bubble_sort_cloud_turb(item1 *curr2);
item1 *llist_bubble_sort_contour_group(item1 *curr2);
char  *getGroupType(int grptyp);
char  *getSymType(VG_DBStruct *el);
void getMatchContAttr(char *tblArray[], int *grpColor, char* contArray[], int tblArrayLen, int *ier);
void getMatchGrptyp(char *lineGrptyp[], int *grptyp, int lineGrptypLen, char* grptypArray[], int *ier);

void convertElm(item1 *curr2, char *buffer, FILE *ofptr, char *tblArray[], int tblArrayLen);
void getCcf(VG_DBStruct *el, char *buffer, int *ier);
void getWatch(VG_DBStruct *el, char *buffer, int *grpNum, int *ier);
void getContour(VG_DBStruct *el, char *buffer, int *contText, int *grpNum, int *grpColor, char *tblArray[], int tblArrayLen, int *ier);
void getSymLab(VG_DBStruct *el, char *buffer, int *symText, int *grpNum, int *ier);
void getOutlooks(VG_DBStruct *el, char *buffer, char* outType, int *outlookText, int *grpNum, int *ier);
void getCommonCollection(VG_DBStruct *el, char *buffer, int *commonText, int *grpNum, int *ier);
void getCloud(VG_DBStruct *el, char *buffer, int *cloudText, int *grpNum, int *grpInit, int *ier);
void getTurb(VG_DBStruct *el, char *buffer, int *turbText, int *grpNum, int *grpInit, int *ier);
void getVolc(VG_DBStruct *el, char *buffer, int *vol, int *ashFhr, int *ier);
void writeEndingCollection(VG_DBStruct *el, FILE *ofptr, int *grpNum, int *grpInit, int *grpColor, int *contText, int *symText, int *outlookText, int *commonText, int *cloudText, int *turbText, int *ier);
void writeEndingAsh(VG_DBStruct *el, FILE *ofptr, int *ier);


/************************************************************************
 * vgf2geojson.c								*
 *									*
 * CONTENTS:								*
 ***********************************************************************/
/*
 *---------------------------------------------------------------------*
 *                   Public interface for vgfToXML                     *
 *---------------------------------------------------------------------*
 */

/*=====================================================================*/

int vgfToGeoJson(char *vgfn, char *asfn, char *activity, char *subActivity, char *contTbl)

/********************************************************************************
 * vgfToGeoJson									*
 *										*
 * This program is a re-write of VGF2TAG and vgfToXml.				*
 *										*
 * Log: 									*
 * Q.Zhou /Chug         01/10   modified from vgf2tag to generate xml   	*
 * Q.Zhou /Chug         06/10   Added tag handling for collections      	*
 * Q.Zhou /Chug         09/10   Added method to read all els to a list. 	*
 *                              Added method to sort the list           	*
 * Q.Zhou /Chug         10/10   Increased sort conditions for Cloud grp 	*
 *                              Added Cloud, Outlook, volcano, ccfp...  	*
 * Q.Zhou /Chug         12/10   Handle grptyp 5,6,8 to contour, symLabel	*
 *                              Symbol H,L with grptyp 5,6 are contour  	*
 * Q.Zhou /Chug         12/10   Added sort for contour w. grouped lines 	*
 * Q.Zhou /Chug         02/11   Handled contour line & text n*n match   	*
 *				Add common collection for cave non handled grps *
 * Q.Zhou /Chug         02/11   Added/modified watch box ending tag     	*
 *				Modified common collection for cloud, outlook.. * 
 * Q.Zhou /Chug         04/11   Added grptyp 9(Tropical),27(Isobars) to contours*
 *                              Refined contour, outlook, common collection rule*
 * Q.Zhou /Chug		11/11   Added parameter activity and subActivity.       *
 *                              Changed CCF to add collection and contents      *
 * Q.Zhou /Chug		12/11   Added contour attrubutes. Added read from table *
 * Q.Zhou /Chug		12/11   Handled vgf delete situation                    *
 *                              Handled group with single symbol -- normal DE   *
 * Q.Zhou /Chug		03/12   Added grptyp & contour to the commandline table *
 * Q.Zhou /Chug         06/12   Added head only vgf conversion                  *
 * Q.Zhou /Chug         08/12   Sigmet line width unit changed                  *
 * M.James/Unidata	04/14	Copied from vgfToXml				*
 ********************************************************************************/
{
    int		nw, more, ier;
    int		wrtflg, curpos;
    VG_DBStruct	el ; 
    char		buffer[10000]; 
    char		infile[128], ifname[128];
    long		ifilesize;
    FILE		*ifptr, *ofptr;

    item1           *curr2, *head1, *head2; /* head1=begin, head2=end */

/*---------------------------------------------------------------------*/

    /*
     *  Opening the input VGF file  
     */
    wrtflg = 0;
    cvg_openj ( vgfn, wrtflg, &(ifptr), &ier );

    if ( ier != 0 )  {
        printf("Error opening VGF file %s\n", infile );
        exit (0);
    }

    cfl_inqr ( vgfn, NULL, &ifilesize, ifname, &ier );

    ofptr = cfl_wopn ( asfn, &ier );

    nw = 0;
    more = G_TRUE;
    curpos = 0;
    ifptr = (FILE *) cfl_ropn(ifname, "", &ier);
   
    
    /*
     * Get productType string - activity(subActivity). 
     */
    char *productType;
    productType = (char *)malloc(128);

    if (activity != NULL && strlen(activity) != 0 && strcmp(activity, "Default")!=0 && strcmp(activity, "default")!=0) {
	if (subActivity != NULL && strlen(subActivity) != 0 
		&& strcmp(subActivity, "Default")!=0 && strcmp(subActivity, "default")!=0)
	    sprintf(productType, "%s%s%s%s", activity, "(", subActivity, ")");
	else
	    productType = activity;
    }
    else {
	productType = "Default";
    }   

   
    /*
     *  Load the specified table - This is used for converting Contours or
     *  converting a group type 8 (LABEL) into a CAVE outlook element - normally
     *  a group type 8 will be converted into Contours element in CAVE. 
     *  
     *  If a VGF file contains grouped lines and labels, they will be checked for 
     *  group type against this table. If a match is found, the matching information 
     *  will be used to perform the conversion. 
     *
     *  groupIndicator		groupType	convertTo	convertedTypeName
     *  group				8	outlook		FLOOD
     *
     * Contour conversion information
     * To get contour parameter, level, forecast hour ... according to the contour line color
     * 
     * If a VGF file contains contour lines, they will be checked against the contour 
     * color on this table. If a match is found, the information from the rest of the 
     * row will be used to convert into an XML file.
     * 
     * color  	param   level1    level2    fcstHr  cint 		time1    time2
     * 5  	HGHT    1000        -1      f000    60/0/100	20111213/1200    -1
     * 6     	HGHT    1000       500      f024     6/0/100	20111213/1200    -1
     * 2     	TEMP     850        -1      f018     3/0/100	20111213/1200    -1
     * 3     	PRES       0        -1      f012     4/0/100	20111213/1200 	 -1
     *  
     */
    char *tblArray[64];   //store vgfConvert.tbl info
    
    int i = 0;
    char fname[128], contTitle[256]=""; 
    FILE *fptr;
    long filesize;
    int iret; 
       
    if (contTbl != NULL && strcmp(contTbl, "") != 0) {
	cfl_inqr ( contTbl, NULL, &filesize, fname, &iret );
	if ( iret == 0 ) {	
    	    fptr = (FILE *) cfl_ropn(fname, "", &iret);

    	    if (iret == 0) {
	        while (!feof ( fptr ) ) {

    	    	    cfl_rdln( fptr, sizeof(contTitle), contTitle, &iret );
		    if ( iret == 0 && contTitle[0] != '!') {
		       tblArray[i] = (char*)malloc(sizeof(contTitle));
		       strcpy(tblArray[i], contTitle);		
		    
		       i++;
		    } 
	        }
	    }
	    
	    fclose(fptr);
    	}
	else {
            printf( "Warning: cannot find %s in current directory!\n", fname );	
	}
    }


    /*
     * Read VG file header and write out an XML header. 
     */
    cvg_rdjrecnoc( ifname, ifptr, curpos, &el, &ier );

    if (el.hdr.vg_type == FILEHEAD_ELM) {   
	
    	sprintf( buffer, "{ 'type': 'FeatureCollection',\n  'features': \[\n", productType, productType);
	
    	cfl_writ ( ofptr, (int)strlen(buffer), (unsigned char*)buffer, &ier );
   	curpos += el.hdr.recsz;  //424
    }


    /*
     * Read all other elements in the VG file 
     */
    head1 = NULL;
    head2 = NULL;
    curr2 = NULL;

    while ( nw < MAX_EDITABLE_ELEMS && more == G_TRUE )  {
	cvg_rdjrecnoc( ifname, ifptr, curpos, &el, &ier );

	if ( ier < 0 )  {
            more = G_FALSE;
	}
	else {
	    if (el.hdr.vg_type != FILEHEAD_ELM && el.hdr.delete ==0) {
	    	curpos += el.hdr.recsz;
	    	curr2 = (item1 *)malloc(sizeof(item1));     		
	    	curr2->el =el;

	    	//add curr2 to head1 head2 list
		if (head1 == NULL) {
    		    head1 = head2 = curr2;
    		    head1->next = NULL;
		}
		else {	
	    	    head2->next = curr2;
	    	    head2 = curr2;
	    	    head2->next = NULL;
		}
	    }
	    else if (el.hdr.vg_type == FILEHEAD_ELM && el.hdr.delete ==0) { //second FILEHEAD, stop
		break;
	    }
	    else {          // don't read if delete !=0
		curpos += el.hdr.recsz;
	    }
      	}

 	nw++;			
    }
    
    /*
     *  If there is only a header in the VG file, end the XML file.
     *  Otherwise, sort the list of VG elements.
     */
    if ( nw <= 1 ) {
        sprintf( buffer, "        </DrawableElement>\n      </Layer>\n    </Product>\n  </Products>\n");

    	cfl_writ ( ofptr, (int)strlen(buffer), (unsigned char*)buffer, &ier );
    	printf("The vgf file %s has no Pgen content.\n", vgfn);
    	fclose(ofptr);

    	return ( 0 );
    }
    else {
     	head1 = llist_bubble_sort(head1); 
   
    	// sort cloud and turb further more
     	head1 = llist_bubble_sort_cloud_turb(head1); 
    }
    

    /*
     *  Convert VG element list into CAVE elements in XML.
     */
    curr2 = head1;
    convertElm(curr2, buffer, ofptr, tblArray, i);

  
    /*
     * free spaces
     */
    int j = 0;
    for (j=0; j<i; j++)
	free (tblArray[j]);

    fclose(ofptr);
    
    return ( 0 );

}


