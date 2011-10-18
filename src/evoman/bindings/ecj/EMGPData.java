package evoman.bindings.ecj;

import ec.gp.GPData;
import evoman.tools.dtype.*;

public class EMGPData extends GPData implements DType{

	private static final long serialVersionUID = 1L;
	protected DType _value;

	public EMGPData(){
		_value = new DoubleType(0.0);
	}
	
	public EMGPData(DType d){
		_value = d;
	}

	@Override
	public void copyTo(GPData gpd) {
		( (EMGPData) gpd )._value = this._value;
	}

	@Override
	public Boolean asBoolean() {
		return _value.asBoolean();
	}

	@Override
	public Double asDouble() {
		return _value.asDouble();
	}

	@Override
	public Integer asInteger() {
		return _value.asInteger();
	}

	@Override
	public String asString() {
		return _value.asString();
	}

	@Override
	public void fromBoolean(Boolean b) {
		_value.fromBoolean(b);
	}

	@Override
	public void fromInteger(Integer i) {
		_value.fromInteger(i);
	}

	@Override
	public void fromDouble(Double d) {
		_value.fromDouble(d);
	}

	@Override
	public void fromString(String s) {
		_value.fromString(s);
	}

	@Override
	public void fromDType(DType t) {
		_value.fromDType(t);
	}
	

}
