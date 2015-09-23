#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.common.serializer;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public enum LongSerializer implements RedisSerializer<Long> {
	INSTANCE;

	@Override
	public byte[] serialize(Long aLong) throws SerializationException {
		if (null != aLong) {
			return aLong.toString().getBytes();
		} else {
			return new byte[0];
		}
	}

	@Override
	public Long deserialize(byte[] bytes) throws SerializationException {
		if (bytes.length > 0) {
			return Long.parseLong(new String(bytes));
		} else {
			return null;
		}
	}
}