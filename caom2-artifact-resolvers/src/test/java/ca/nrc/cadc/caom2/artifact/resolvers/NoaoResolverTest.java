/*
************************************************************************
*******************  CANADIAN ASTRONOMY DATA CENTRE  *******************
**************  CENTRE CANADIEN DE DONNÉES ASTRONOMIQUES  **************
*
*  (c) 2017.                            (c) 2017.
*  Government of Canada                 Gouvernement du Canada
*  National Research Council            Conseil national de recherches
*  Ottawa, Canada, K1A 0R6              Ottawa, Canada, K1A 0R6
*  All rights reserved                  Tous droits réservés
*
*  NRC disclaims any warranties,        Le CNRC dénie toute garantie
*  expressed, implied, or               énoncée, implicite ou légale,
*  statutory, of any kind with          de quelque nature que ce
*  respect to the software,             soit, concernant le logiciel,
*  including without limitation         y compris sans restriction
*  any warranty of merchantability      toute garantie de valeur
*  or fitness for a particular          marchande ou de pertinence
*  purpose. NRC shall not be            pour un usage particulier.
*  liable in any event for any          Le CNRC ne pourra en aucun cas
*  damages, whether direct or           être tenu responsable de tout
*  indirect, special or general,        dommage, direct ou indirect,
*  consequential or incidental,         particulier ou général,
*  arising from the use of the          accessoire ou fortuit, résultant
*  software.  Neither the name          de l'utilisation du logiciel. Ni
*  of the National Research             le nom du Conseil National de
*  Council of Canada nor the            Recherches du Canada ni les noms
*  names of its contributors may        de ses  participants ne peuvent
*  be used to endorse or promote        être utilisés pour approuver ou
*  products derived from this           promouvoir les produits dérivés
*  software without specific prior      de ce logiciel sans autorisation
*  written permission.                  préalable et particulière
*                                       par écrit.
*
*  This file is part of the             Ce fichier fait partie du projet
*  OpenCADC project.                    OpenCADC.
*
*  OpenCADC is free software:           OpenCADC est un logiciel libre ;
*  you can redistribute it and/or       vous pouvez le redistribuer ou le
*  modify it under the terms of         modifier suivant les termes de
*  the GNU Affero General Public        la “GNU Affero General Public
*  License as published by the          License” telle que publiée
*  Free Software Foundation,            par la Free Software Foundation
*  either version 3 of the              : soit la version 3 de cette
*  License, or (at your option)         licence, soit (à votre gré)
*  any later version.                   toute version ultérieure.
*
*  OpenCADC is distributed in the       OpenCADC est distribué
*  hope that it will be useful,         dans l’espoir qu’il vous
*  but WITHOUT ANY WARRANTY;            sera utile, mais SANS AUCUNE
*  without even the implied             GARANTIE : sans même la garantie
*  warranty of MERCHANTABILITY          implicite de COMMERCIALISABILITÉ
*  or FITNESS FOR A PARTICULAR          ni d’ADÉQUATION À UN OBJECTIF
*  PURPOSE.  See the GNU Affero         PARTICULIER. Consultez la Licence
*  General Public License for           Générale Publique GNU Affero
*  more details.                        pour plus de détails.
*
*  You should have received             Vous devriez avoir reçu une
*  a copy of the GNU Affero             copie de la Licence Générale
*  General Public License along         Publique GNU Affero avec
*  with OpenCADC.  If not, see          OpenCADC ; si ce n’est
*  <http://www.gnu.org/licenses/>.      pas le cas, consultez :
*                                       <http://www.gnu.org/licenses/>.
*
*  $Revision: 5 $
*
************************************************************************
*/

package ca.nrc.cadc.caom2.artifact.resolvers;

import ca.nrc.cadc.util.Log4jInit;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author hjeeves
 */
public class NoaoResolverTest {
    private static final Logger log = Logger.getLogger(NoaoResolverTest.class);

    static {
        Log4jInit.setLevel("ca.nrc.cadc", Level.INFO);
    }

    String VALID_URI = "noao:FOO";
    String VALID_URI2 = "noao:FOO/bar";

    //    URL: http://archive1.dm.noao.edu:7003/?fileRef=c13a_140805_212140_ori.fits.fz
    //    URI: noao:c13a_140805_212140_ori.fits.fz
    String PROTOCOL_STR = "http";
    String BASE_ARTIFACT_URL = "archive1.dm.noao.edu";
    String URL_QUERY = "fileRef=";
    String BASE_PATH = "/";

    // There are no tests that will validate the content of the
    // path other than empty.
    String INVALID_URI_BAD_SCHEME = "ad:FOO/Bar";

    NoaoResolver noaoResolver = new NoaoResolver();

    public NoaoResolverTest() {
    }

    @Test
    public void testGetScheme() {
        Assert.assertTrue(noaoResolver.getScheme().equals(noaoResolver.getScheme()));
    }

    @Test
    public void testValidURI() {
        try {
            List<String> validURIs = new ArrayList<String>();
            validURIs.add(VALID_URI);
            validURIs.add(VALID_URI2);

            for (String uriStr : validURIs) {
                URI uri = new URI(uriStr);
                URL url = noaoResolver.toURL(uri);

                // NOAO uses '?' to POST scheme specific part of the URI to the server
                log.debug("toURL returned: " + url.toString());

                Assert.assertEquals(url.toString(),PROTOCOL_STR + "://" + BASE_ARTIFACT_URL + ":7003/?" + URL_QUERY + uri.getSchemeSpecificPart() );
                Assert.assertEquals(URL_QUERY + uri.getSchemeSpecificPart(), url.getQuery());
                Assert.assertEquals(BASE_ARTIFACT_URL, url.getHost());
                Assert.assertEquals(BASE_PATH, url.getPath());
                Assert.assertEquals(PROTOCOL_STR, url.getProtocol());
            }
        } catch (Exception unexpected) {
            log.error("unexpected exception", unexpected);
            Assert.fail("unexpected exception: " + unexpected);
        }
    }

    @Test
    public void testInvalidURIBadScheme() {
        try {
            URI uri = new URI(INVALID_URI_BAD_SCHEME);
            URL url = noaoResolver.toURL(uri);
            Assert.fail("expected IllegalArgumentException, got " + url);
        } catch (IllegalArgumentException expected) {
            log.info("IllegalArgumentException thrown as expected. Test passed.: " + expected);
        } catch (Exception unexpected) {
            log.error("unexpected exception", unexpected);
            Assert.fail("unexpected exception: " + unexpected);
        }
    }

    @Test
    public void testInvalidNullURI() {
        try {
            URL url = noaoResolver.toURL(null);
            Assert.fail("expected IllegalArgumentException, got " + url);
        } catch (IllegalArgumentException expected) {
            log.info("IllegalArgumentException thrown as expected. Test passed.: " + expected);
        } catch (Exception unexpected) {
            log.error("unexpected exception", unexpected);
            Assert.fail("unexpected exception: " + unexpected);
        }
    }

}
