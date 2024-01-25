package carShowroom;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;


@DataType()
public class Car {
	//Variable Declarations
	@Property()
	private final String id;
		
	@Property()
	private final String model;
		
	@Property()
	private final String owner;
		
	@Property()
	private final String value;
		
	//Getter
	public String getId() {
		return id;
	}
		
	public String getModel() {
		return model;
	}
		
	public String getOwner() {
		return owner;
	}
		
	public String getValue() {
		return value;
	}
		
	//Function to initiate variables
	public Car(@JsonProperty("id") final String id, @JsonProperty("model") final String model, @JsonProperty("owner") final String owner, @JsonProperty("value") final String value) {
		this.id = id;
		this.model = model;
		this.owner = owner;
		this.value = value;		
	}
}
