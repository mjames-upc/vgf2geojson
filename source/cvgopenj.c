#include "cvgcmn.h"

void cvg_rdjhdr ( char *fname, FILE *fp, int start, int size, 
				VG_DBStruct *el, int *flag, int *iret );


void cvg_openj ( char *filnam, int wrtflg, FILE **fptr, int *iret )
/************************************************************************
 * cvg_openj								*
 *									*
 * This function opens a VG file.					*
 * Note: It doesn't attempt to create a vg file if specified file       *
 *       doesn't exist.			                                *
 *                                                                      *
 * cvg_openj ( filnam, wrtflg, fptr, iret )				*
 *									*
 * Input parameters:							*
 *	*filnam		char		VG filename			*
 *	wrtflg		int		Write flag			*
 *									*
 * Output parameters:							*
 *	**fptr		FILE		File pointer			*
 *	*iret		int		Return code			*
 *					   2 = created vg file		*
 *					   0 = normal 			*
 *					  -1 = error opening VG file	*
 *					 -13 = error verifying vg file  *
 *					 -25 = no filehead elm in file  *
 *					 -47 = no file name specified   *
 **									*
 * Log:									*
 * D. Keiser/GSC	 1/97	Copied from UTF_OPEN			*
 * E. Wehner/EAi	 6/97	Create VGF if does not exist		*
 * E. Wehner/EAi	 7/97	Produce error msg on bad hdr		*
 * E. Wehner/EAi	 9/97	Handle null file name			*
 * E. Safford/GSC	10/98	clean up & rework error mesgs		*
 * A. Hardy/GSC          1/01   changed fptr from int to FILE   	*
 * E. Safford/SAIC	04/02	avoid cvg_rdhdr if bad filehead 	*
 * T. Lee/SAIC		11/03	used cvgcmn.h				*
 * T. Piper/SAIC	09/06	Added wrtflg check after first cfl_inqr	*
 * Q. Zhou/Chug         01/10   remove er_lmsg and cvg_crvgf function   *
 ***********************************************************************/
{
    int			ier, flag, curpos;
    long		maxbytes;
    int			err_log, dtl_log, dbg_log;
    VG_DBStruct		el;
    char		grp[4], newfil[256];
/*---------------------------------------------------------------------*/
    *fptr = NULL;

    strcpy(grp, "CVG");
    err_log  = 0;
    dtl_log  = 2;
    dbg_log  = 4; 

    *iret = 0;

    /*  Check to see if there is a file of the specified name, 
     *  open it if it is there.
     */    

    if ( !filnam) {
	*iret  = -47;
	return;
    }

    cfl_inqr(filnam, NULL, &maxbytes, newfil, &ier);
    
    if ( ier < 0 && wrtflg == G_TRUE ) {

	if (ier == 0) {

	     
	     /* Verify file was created */
	      
	    cfl_inqr(filnam, NULL, &maxbytes, newfil, &ier);

	}

	if (ier != 0) {
	    *iret  = -1;
	    return;
	}
	else {
	    
	     /*  File was created successfully	*/     
	    *iret = 2;

	}
    }

    /*  
     *  Open file with either read, or read-write permissions
     */

    if (wrtflg == G_TRUE) {

	*fptr = cfl_uopn(filnam, &ier);
    }
    else {

	*fptr = cfl_ropn(filnam, " ", &ier);
    }

    if (( ier != 0 ) || ( *fptr == NULL )) {		// open failed 
	*iret = -1;
 
    }
    else {

	if ( (size_t)maxbytes < (sizeof(FileHeadType) + sizeof(VG_HdrStruct)) ) {
	    *iret = -25;
	}
        else {
            /* 
	     * Check to see that the file is a valid VGF file...
	     */

	    curpos = 0;
            cvg_rdjhdr(newfil, *fptr, curpos, (int)maxbytes, &el, &flag, &ier);

	    if (ier != 0) {
                *iret = -13;
 
    	    }

	    if (el.hdr.vg_type != FILEHEAD_ELM) {
	        *iret = -25;
 
	    }
	}

    }
}
