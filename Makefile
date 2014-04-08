#
# This Make file is to install A2 converter in the $GEMPAK/utilities/a2conv
# and create the latest library file libVgfXml.so 
# To do this, make sure you have the latest awips2-converter-installer.tgz download and unzipped,
# and run this Makefile in this directory
#

all: cpBin mvLib 

cpBin: /home/gempak/vgf2json
	cp /home/gempak/vgf2json/vgf2geo ${OS_BIN}

mvLib: /home/gempak/vgf2json/buildfile.sh 
	sh /home/gempak/vgf2json/buildfile.sh
