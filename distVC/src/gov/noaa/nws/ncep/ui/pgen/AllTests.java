package gov.noaa.nws.ncep.ui.pgen;

import gov.noaa.nws.ncep.ui.pgen.display.DisplaySuite;
import gov.noaa.nws.ncep.ui.pgen.elements.ElementsSuite;
import gov.noaa.nws.ncep.ui.pgen.gfa.GfaSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@Suite.SuiteClasses({DisplaySuite.class, ElementsSuite.class, GfaSuite.class})
public class AllTests
{
}

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.AllTests
 * JD-Core Version:    0.6.2
 */