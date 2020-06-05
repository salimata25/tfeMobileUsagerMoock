package sn.diotali.tfe_usager_dgid.services;


/**
 * Created by Cheikhouna on 02/02/2018.
 */

public class ServiceParams {

    private String methodName;

    private MethodParams methodParams;

    public ServiceParams() {
        super();
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public MethodParams getMethodParams() {
        return methodParams;
    }

    public void setMethodParams(MethodParams methodParams) {
        this.methodParams = methodParams;
    }

}
