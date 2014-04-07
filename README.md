
# vgf2json , the NAWIPS VG to GeoJSON converter

##Usage 

    vgf2json <inputfile> outputdir  - which will write a file with the name <inputfile>.json

##Purpose.

The PGEN VGF files are used in legacy AWIPS system.  The PGEN data XML files are used 
in the new AWIPSII system.  The converters will convert PGEN data files from one type of 
format to another, so the data can be displayed and transferred between the two systems.  
Meanwhile, the existing applications that use the VGF files remain unchanged.

Note 1. You can convert an entire directory of VG files to GeoJSON by defining <inputfile> as a directory.
Note 2. Only *.vgf files can be converted. 
Note 3. The converted files are renamed to *.json and stored in the 
        destination directory/path. The conversion overwrites the same 
		named files.
