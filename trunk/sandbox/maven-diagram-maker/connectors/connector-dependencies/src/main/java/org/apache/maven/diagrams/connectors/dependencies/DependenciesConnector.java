package org.apache.maven.diagrams.connectors.dependencies;

import org.apache.maven.diagrams.connector_api.ConnectorConfiguration;
import org.apache.maven.diagrams.connector_api.ConnectorException;
import org.apache.maven.diagrams.connector_api.DiagramConnector;
import org.apache.maven.diagrams.connector_api.DynamicDiagramConnector;
import org.apache.maven.diagrams.connector_api.descriptor.ConnectorDescriptor;
import org.apache.maven.diagrams.graph_api.Graph;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.logging.AbstractLogEnabled;

public class DependenciesConnector extends AbstractLogEnabled implements DiagramConnector
{
    public ConnectorDescriptor connectorDescriptor;

    public MavenProject mavenProject;

    public Graph calculateGraph( ConnectorConfiguration arg0 ) throws ConnectorException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public DynamicDiagramConnector getDynamicDiagramConnector() throws ConnectorException
    {
        throw new IllegalStateException( "ClassesConnector doesn't suppert dynamic diagram connector" );
    }

    public ConnectorDescriptor getConnectorDescriptor() throws ConnectorException
    {
        return connectorDescriptor;
    }

    public void setMavenProject( MavenProject mavenProject )
    {
        this.mavenProject = mavenProject;
    }

}
