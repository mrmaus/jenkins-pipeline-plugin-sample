package io.jenkins.plugins;


import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import org.jenkinsci.lib.configprovider.model.ConfigFile;
import org.jenkinsci.lib.configprovider.model.ConfigFileManager;
import org.jenkinsci.plugins.durabletask.BourneShellScript;
import org.jenkinsci.plugins.durabletask.DurableTask;
import org.jenkinsci.plugins.durabletask.WindowsBatchScript;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.jenkinsci.plugins.workflow.steps.durable_task.DurableTaskStep;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.Serializable;

/**
 * todo:
 */
public class MavenStep extends DurableTaskStep implements Serializable {

  private final String script;
  private volatile boolean unix;

  @DataBoundConstructor
  public MavenStep(String script) {
    this.script = script;
  }

  public String getScript() {
    return script;
  }

  @Override
  protected DurableTask task() {
    if (unix) {
      return new BourneShellScript(script);
    }
    return new WindowsBatchScript(script);
  }

  @Override
  public StepExecution start(StepContext context) throws Exception {
    final ConfigFile configFile = new ConfigFile("maven-settings", "settings.xml", true);

    final EnvVars envVars = context.get(EnvVars.class);
    final Run run = context.get(Run.class);
    final FilePath filePath = context.get(FilePath.class);
    final TaskListener taskListener = context.get(TaskListener.class);

    final Launcher launcher = context.get(Launcher.class);
    unix = launcher != null && launcher.isUnix();

    // result contains full path to the written file
    final FilePath result = ConfigFileManager.provisionConfigFile(configFile, envVars, run,
        filePath, taskListener, null);

    return super.start(context);
  }

  @Extension
  public static final class DescriptorImpl extends DurableTaskStepDescriptor {

    @Override
    public String getDisplayName() {
      return "Maven";
    }

    @Override
    public String getFunctionName() {
      return "maven";
    }

  }

}
