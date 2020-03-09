package models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Message {


    private String greeting;


    public String getGreeting() {
        return greeting;
    }
    @XmlElement
    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }
}
