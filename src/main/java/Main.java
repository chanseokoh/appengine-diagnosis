import com.google.cloud.tools.appengine.operations.cloudsdk.serialization.CloudSdkComponent;
import com.google.cloud.tools.managedcloudsdk.ManagedCloudSdk;
import com.google.cloud.tools.managedcloudsdk.ManagedSdkVerificationException;
import com.google.cloud.tools.managedcloudsdk.ManagedSdkVersionMismatchException;
import com.google.cloud.tools.managedcloudsdk.UnsupportedOsException;
import com.google.cloud.tools.managedcloudsdk.command.CommandCaller;
import com.google.cloud.tools.managedcloudsdk.command.CommandExecutionException;
import com.google.cloud.tools.managedcloudsdk.command.CommandExitException;
import java.util.Arrays;
import java.util.List;

public class Main {

  public static void main(String[] args)
      throws InterruptedException, UnsupportedOsException, ManagedSdkVerificationException,
          ManagedSdkVersionMismatchException, CommandExecutionException, CommandExitException {
    ManagedCloudSdk sdk = ManagedCloudSdk.newManagedSdk();
    System.out.println("SDK home: " + sdk.getSdkHome());
    System.out.println("gcloud path: " + sdk.getGcloudPath());

    if (!sdk.isInstalled()) System.out.println("Error: SDK is not installed??");

    List<String> cmd = Arrays.asList(sdk.getGcloudPath().toString(),
        "components", "list", "--format=json", "--filter=state.name:Update Available");

    String result = null;
    try {
      result = CommandCaller.newCaller().call(cmd, null, null);
      System.out.println("BEGIN result");
      System.out.println(result);
      System.out.println("END result");
    } catch (CommandExitException e) {
      System.out.println("*** CommandExitException; error log = [" + e.getErrorLog() + "] ***");
      e.printStackTrace();
    } catch (CommandExecutionException e) {
      System.out.println("*** CommandExecutionException; error log = [" + e.getErrorLog() + "] ***");
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    for (CloudSdkComponent component : CloudSdkComponent.fromJsonList(result)) {
      System.out.println("name: " + component.getName());
    }
  }
}
