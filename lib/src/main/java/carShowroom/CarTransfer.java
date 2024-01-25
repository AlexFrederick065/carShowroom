package carShowroom;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import com.owlike.genson.Genson;
 
 
@Contract(
        name = "carShowroom",
        info = @Info(
                title = "carShowroom contract",
                description = "A Sample Car transfer chaincode example",
                version = "0.0.1-SNAPSHOT"))
 
 
@Default
//implements is a inheritance keyword
public final class CarTransfer implements ContractInterface {
 
	private final Genson genson = new Genson();
	private enum carShowroomErrors {
	        Car_NOT_FOUND,
	        Car_ALREADY_EXISTS
	    }
	
	
	/**
     * Add some initial properties to the ledger
     *
     * @param ctx the transaction context
     */
	//This function is used to initialize the chaincode and without it we cannot access other function
    @Transaction()
    public void initLedger(final Context ctx) {
    	ChaincodeStub stub= ctx.getStub();
        
        Car Car = new Car("1", "Maruti","Mark","6756");
        
        String CarState = genson.serialize(Car);
        
        stub.putStringState("1", CarState);
    }
    
    
    /**
     * Add new Car on the ledger.
     *
     * @param ctx the transaction context
     * @param id the key for the new Car
     * @param model the model of the new Car
     * @param ownername the owner of the new Car
     * @param value the value of the new Car
     * @return the created Car
     */
	
    @Transaction()
    public Car addNewCar(final Context ctx, final String id, final String model,
            final String ownername, final String value) {
        //getStub gives instance of blockchain
    	ChaincodeStub stub = ctx.getStub();
 
        String CarState = stub.getStringState(id);
        
        if (!CarState.isEmpty()) {
            String errorMessage = String.format("Car %s already exists", id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, carShowroomErrors.Car_ALREADY_EXISTS.toString());
        }
        
        Car Car = new Car(id, model, ownername,value);
        //Below line convert java object to json string because fabric accepts json string 
        CarState = genson.serialize(Car);
        //putStringState is used for writing
        stub.putStringState(id, CarState); 
        
        return Car;
    }
 
 
    	/**
	     * Retrieves a Car based upon Car Id from the ledger.
	     *
	     * @param ctx the transaction context
	     * @param id the key
	     * @return the Car found on the ledger if there was one
	     */
    	@Transaction()
	    public Car queryCarById(final Context ctx, final String id) {
    		//Below line is used to connect to the ledger
	        ChaincodeStub stub = ctx.getStub();
	        String CarState = stub.getStringState(id);
 
	        if (CarState.isEmpty()) {
	            String errorMessage = String.format("Car %s does not exist", id);
	            System.out.println(errorMessage);
	            throw new ChaincodeException(errorMessage, carShowroomErrors.Car_NOT_FOUND.toString());
	        }
	        // Below line is optional and is used to convert json to java object in order to be read by java properly
	        Car Car = genson.deserialize(CarState, Car.class);
	        
//	        try json.dump
	        return Car;
	    }
    	
        /**
   	     * Changes the owner of a Car on the ledger.
   	     *
   	     * @param ctx the transaction context
   	     * @param id the key
   	     * @param newOwner the new owner
   	     * @return the updated Car
   	     */
   	    @Transaction()
   	    public Car changeCarOwnership(final Context ctx, final String id, final String newCarOwner) {
   	        ChaincodeStub stub = ctx.getStub();
 
   	        String CarState = stub.getStringState(id);
 
   	        if (CarState.isEmpty()) {
   	            String errorMessage = String.format("Car %s does not exist", id);
   	            System.out.println(errorMessage);
   	            throw new ChaincodeException(errorMessage, carShowroomErrors.Car_NOT_FOUND.toString());
   	        }
   	        
 
   	        Car Car = genson.deserialize(CarState, Car.class);
 
   	        Car newCar = new Car(Car.getId(), Car.getModel(), newCarOwner, Car.getValue());
   	        
   	        String newCarState = genson.serialize(newCar);
   	        
   	        stub.putStringState(id, newCarState);
 
   	        return newCar;
   	    } 
   	    
   	    @Transaction()
   	    public Car changeCarValue(final Context ctx, final String id, final String value) {
   	    	ChaincodeStub stub = ctx.getStub();
   	    	
   	    	String CarState = stub.getStringState(id);
   	    	
   	    	if(CarState.isEmpty()) {
   	    		String errMsg = String.format("No Such Car Exists", id);
   	    		System.out.println(errMsg);
   	    	}
   	    	
   	    	Car Car = genson.deserialize(CarState, Car.class);
   	    	
   	    	Car newCar = new Car(Car.getId(),Car.getModel(),Car.getOwner(),value);
   	    	
   	    	String newCarState = genson.serialize(newCar);
   	    	
   	    	stub.putStringState(id, newCarState);
   	     
   	        return newCar;
   	    }
}
