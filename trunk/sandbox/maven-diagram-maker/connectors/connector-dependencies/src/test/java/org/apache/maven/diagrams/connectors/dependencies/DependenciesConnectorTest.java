package org.apache.maven.diagrams.connectors.dependencies;

import org.apache.maven.diagrams.connector_api.DiagramConnector;
import org.codehaus.plexus.PlexusTestCase;

public class DependenciesConnectorTest extends PlexusTestCase {

	public void testLookupComponent() throws Exception {
		assertNotNull(lookup(DiagramConnector.class, "connector-dependencies"));
	}
}
