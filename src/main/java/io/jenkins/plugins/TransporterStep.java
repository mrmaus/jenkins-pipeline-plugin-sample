package io.jenkins.plugins;

import hudson.Extension;
import hudson.model.Result;
import hudson.model.TaskListener;
import org.jenkinsci.plugins.workflow.steps.AbstractStepDescriptorImpl;
import org.jenkinsci.plugins.workflow.steps.AbstractStepImpl;
import org.jenkinsci.plugins.workflow.steps.AbstractSynchronousNonBlockingStepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import javax.inject.Inject;
import java.io.PrintStream;

public class TransporterStep extends AbstractStepImpl {

  private String action = DescriptorImpl.action;

  @DataBoundConstructor
  public TransporterStep() {
  }

  public String getAction() {
    return action;
  }

  @DataBoundSetter
  public void setAction(String action) {
    this.action = action;
  }

  @Override
  public DescriptorImpl getDescriptor() {
    return (DescriptorImpl) super.getDescriptor();
  }


  @Extension
  public static final class DescriptorImpl extends AbstractStepDescriptorImpl {

    public static final String action = "";

    public DescriptorImpl() {
      super(Execution.class);
    }

    @Override
    public String getFunctionName() {
      return "transporter";
    }
  }

  public static final class Execution extends AbstractSynchronousNonBlockingStepExecution<Void> {

    @Inject
    private transient TransporterStep step;

    @Override
    protected Void run() throws Exception {
      getContext().setResult(Result.UNSTABLE);

      final PrintStream logger = getContext().get(TaskListener.class).getLogger();


      logger.println(">>> LOGGER!!><<<S<S<");

      logger.println(">>>> ACTION :: " + step.action);

      System.out.println("TRANSPORTER EXECUTED");
      return null;
    }
  }


}
