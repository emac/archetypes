#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.common.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class BaseModel implements Serializable {

	private static final long serialVersionUID = 4233954290183742956L;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
