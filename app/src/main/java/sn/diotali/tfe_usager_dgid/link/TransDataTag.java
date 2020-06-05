package sn.diotali.tfe_usager_dgid.link;

/**
 * Created by lixc on 2017/3/28.
 */

public class TransDataTag {

    private String pan;

    public byte[] getAmountTag() {
        return new byte[] {(byte) 0x9F, 0x02};
    }

    public byte[] getTransTypeTag() {
        return new byte[] {(byte) 0x9C};
    }

    public byte[] getCurrencyCodeTag() {
        return new byte[] {0x5F, 0x2A};
    }

    public byte[] getCurrencyExponentTag() {
        return new byte[] {0x5F, 0x36};
    }

    public byte[] getTransDateTag() {
        return new byte[] {(byte) 0x9A};
    }

    public byte[] getTransTimeTag() {
        return new byte[] {(byte) 0x9F, 0x21};
    }

    //set parameter data before get pin block when MSR
    public byte[] getParamDataTag() {
        return new byte[]{0x02, 0x02, 0x01, 0x01, 0x02, 0x03, 0x01, 0x02};
    }

    //get EMV data
    public byte[] getTransDataTagGet() {
        return new byte[]{(byte) 0x9F, 0x27, 0x5A, 0x57};
    }

    public byte[] getTrack2Tag() {
        return new byte[]{(byte) 0x03, 0x05};
    }

    public byte[] getCardTypeTag() {
        return new byte[]{(byte) 0x03, 0x01};
    }

    public byte[] getTransResultTag() {
        return new byte[]{(byte) 0x9F, 0x27};
    }

    public void setPan(String pan) {
        String[] string = pan.split("=");
        this.pan = string[0];
    }

    public String getPan() {
        return pan;
    }
}
