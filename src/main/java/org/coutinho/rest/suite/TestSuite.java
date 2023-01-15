package org.coutinho.rest.suite;

import org.coutinho.rest.core.BaseTest;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("org.coutinho.rest.tests")
public class TestSuite extends BaseTest {

}
