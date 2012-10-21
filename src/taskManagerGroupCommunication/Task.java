package taskManagerGroupCommunication;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * JAXB class - task-element
 * This class is based on a code snippet by Rao and follows his structure with public fields.
 * We have added another constructor and some equality measurements
 * @author BieberFever (based on code snippet by rao)
 */
    @XmlRootElement(name = "task")
    public class Task implements Serializable{
    	private static final long serialVersionUID = 7526472295622771337L;
    	
    	/**
    	 * An alternative to the empty constructor in Rao's code snippet
    	 * @param id The ID of the Task
    	 * @param name The name of the Task
    	 * @param date The date of the Task
    	 * @param status The status of the Task
    	 * @param description The description of the Task
    	 * @param attendants The attendants of the Task
    	 */
    	public Task(String id, String name, String date, String status, String description, String attendants){
    		this.id = id;
    		this.name = name;
    		this.date = date;
    		this.status = status;
    		this.description = description;
    		this.attendants = attendants;
    	}
    	
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