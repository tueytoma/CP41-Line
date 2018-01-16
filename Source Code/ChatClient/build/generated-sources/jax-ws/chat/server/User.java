
package chat.server;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for user complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="user">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="username" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="messages" type="{http://server.chat/}statusMess" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="isAdmin" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="isAgroup" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="isOnline" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "user", propOrder = {
    "username",
    "messages",
    "isAdmin",
    "isAgroup",
    "isOnline"
})
public class User {

    protected String username;
    @XmlElement(nillable = true)
    protected List<StatusMess> messages;
    protected boolean isAdmin;
    protected boolean isAgroup;
    public boolean isOnline;

    /**
     * Gets the value of the username property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of the username property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsername(String value) {
        this.username = value;
    }

    /**
     * Gets the value of the messages property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the messages property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMessages().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StatusMess }
     * 
     * 
     */
    public List<StatusMess> getMessages() {
        if (messages == null) {
            messages = new ArrayList<StatusMess>();
        }
        return this.messages;
    }

    /**
     * Gets the value of the isAdmin property.
     * 
     */
    public boolean isIsAdmin() {
        return isAdmin;
    }

    /**
     * Sets the value of the isAdmin property.
     * 
     */
    public void setIsAdmin(boolean value) {
        this.isAdmin = value;
    }

    /**
     * Gets the value of the isAgroup property.
     * 
     */
    public boolean isIsAgroup() {
        return isAgroup;
    }

    /**
     * Sets the value of the isAgroup property.
     * 
     */
    public void setIsAgroup(boolean value) {
        this.isAgroup = value;
    }

    /**
     * Gets the value of the isOnline property.
     * 
     */
    public boolean isIsOnline() {
        return isOnline;
    }

    /**
     * Sets the value of the isOnline property.
     * 
     */
    public void setIsOnline(boolean value) {
        this.isOnline = value;
    }

}
