/*
 * ShiningPanda plug-in for Jenkins
 * Copyright (C) 2011 ShiningPanda S.A.S.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package jenkins.plugins.shiningpanda;

import hudson.EnvVars;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.TaskListener;
import hudson.model.AbstractBuild;
import hudson.model.Node;

import java.io.IOException;

import org.kohsuke.stapler.DataBoundConstructor;

public class StandardPythonBuilder extends InstalledPythonBuilder
{

    /**
     * Constructor using fields
     * 
     * @param pythonName
     *            The name of the PYTHON
     * @param ignoreExitCode
     *            Do not consider the build as a failure if any of the commands
     *            exits with a non-zero exit code
     * @param command
     *            The command to execute in PYTHON environment
     */
    @DataBoundConstructor
    public StandardPythonBuilder(String pythonName, boolean ignoreExitCode, String command)
    {
        super(pythonName, ignoreExitCode, command);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.shiningpanda.jenkins.python.PythonBuilder#setEnvironment(hudson.EnvVars
     * , hudson.model.AbstractBuild, hudson.model.Node, hudson.Launcher,
     * hudson.model.TaskListener)
     */
    @Override
    protected boolean setEnvironment(EnvVars envVars, AbstractBuild<?, ?> build, Node node, Launcher launcher,
            TaskListener listener) throws InterruptedException, IOException
    {
        // Get the PYTHON installation
        StandardPythonInstallation pi = getPython(build, node, listener, envVars);
        // Validate PYTHON installation
        if (!ShiningPandaUtil.validatePythonInstallation(pi, launcher, listener))
            // Can't go further as PYTHON installation is not valid
            return false;
        // Set PYTHON environment
        pi.setEnvironment(envVars, getPathSeparator(launcher));
        // Ready to go
        return true;
    }

    private static final long serialVersionUID = 1L;

    /**
     * Descriptor for this builder
     */
    @Extension
    public static final class DescriptorImpl extends InstalledPythonBuildStepDescriptor
    {
        /*
         * (non-Javadoc)
         * 
         * @see hudson.model.Descriptor#getDisplayName()
         */
        @Override
        public String getDisplayName()
        {
            return Messages.StandardPythonBuilder_DisplayName();
        }

        /*
         * (non-Javadoc)
         * 
         * @see hudson.model.Descriptor#getHelpFile()
         */
        @Override
        public String getHelpFile()
        {
            return "/plugin/shiningpanda/help/StandardPythonBuilder/help.html";
        }
    }
}
