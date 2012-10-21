package utilzz;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * JAXB class - task-element
 * @author BieberFever (based on codesnippet by rao)
 */
    @XmlRootElement(name = "task")
    public class Task implements Serializable{

        @XmlID
        @XmlAttribute
        public String id;
        
        @XmlAttribute
        public String name;
        
        @XmlAttribute
        public String date;
        
        @XmlAttribute
        public String status;
        
        @XmlElement
        public String description;
        
        @XmlElement
        public String attendants;
        
        //Overriding equals to help recognize two identical tasks
        @Override
        public boolean equals(Object obj) {
        	if ( this == obj ) return true;
        	if (!(obj instanceof Task)) return false;
        	Task that = (Task) obj;
        	return  this.id.equals(that.id) &&
        			this.name.equals(that.name) &&
        			this.date.equals(that.date) &&
        			this.status.equals(that.status) &&
        			this.description.equals(that.description) &&
        			this.attendants.equals(that.attendants);
        }
        
        //Overriding hashcode to be consistent with equals
        @Override
        public int hashCode() {
        	int hash = 17;
            hash = hash * 31 + (id == null ? 0 : id.hashCode());
            hash = hash * 31 + (name == null ? 0 : name.hashCode());
            hash = hash * 31 + (date == null ? 0 : date.hashCode());
            hash = hash * 31 + (status == null ? 0 : status.hashCode());
            hash = hash * 31 + (description == null ? 0 : description.hashCode());
            hash = hash * 31 + (attendants == null ? 0 : attendants.hashCode());
            return hash;
        }
    }