
package chat.server;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for statusMess complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="statusMess">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mess" type="{http://server.chat/}message" minOccurs="0"/>
 *         &lt;element name="online" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "statusMess", propOrder = {
    "mess",
    "online"
})
public class StatusMess {

    protected Message mess;
    protected boolean online;

    /**
     * Gets the value of the mess property.
     * 
     * @return
     *     possible object is
     *     {@link Message }
     *     
     */
    public Message getMess() {
        return mess;
    }

    /**
     * Sets the value of the mess property.
     * 
     * @param value
     *     allowed object is
     *     {@link Message }
     *     
     */
    public void setMess(Message value) {
        this.mess = value;
    }

    /**
     * Gets the value of the online property.
     * 
     */
    public boolean isOnline() {
        return online;
    }

    /**
     * Sets the value of the online property.
     * 
     */
    public void setOnline(boolean value) {
        this.online = value;
    }

}
