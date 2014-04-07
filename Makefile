#
# This Make file is to install A2 converter in the $GEMPAK/utilities/a2conv
# and create the latest library file libVgfXml.so 
# To do this, make sure you have the latest awips2-converter-installer.tgz download and unzipped,
# and run this Makefile in this directory
#

all: cpBin mvLib 

cpBin: ${GEMPAK}/utilities/vgf2geojson
	cp ${GEMPAK}/utilities/vgf2geojson/vgf2geojson ${OS_BIN}

mvLib: ${GEMPAK}/utilities/vgf2geojson/buildfile.sh 
	sh ${GEMPAK}/utilities/vgf2geojson/buildfile.sh
