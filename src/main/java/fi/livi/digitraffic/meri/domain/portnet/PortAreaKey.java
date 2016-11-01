package fi.livi.digitraffic.meri.domain.portnet;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class PortAreaKey implements Serializable{
    private String locode;
    private String portAreaCode;

    public String getLocode() {
        return locode;
    }

    public void setLocode(final String locode) {
        this.locode = locode;
    }

    public String getPortAreaCode() {
        return portAreaCode;
    }

    public void setPortAreaCode(final String portAreaCode) {
        this.portAreaCode = portAreaCode;
    }
}
