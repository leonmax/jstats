package com.adcade.statsd.strategy;

import com.adcade.statsd.bucket.Bucket;
import com.adcade.statsd.transport.Transport;



public class InstantStrategy implements Strategy {
	private Transport transport;

	public void setTransport(Transport transport){
		this.transport = transport;
	}

	public <T extends Bucket> boolean send(
			Class<T> clazz, 
			String bucketname, 
			int value, 
			String message){
		try {
			T bucket = clazz.newInstance();
			bucket.setName(bucketname);
			bucket.infuse(value, message);
			transport.doSend(bucket.toString());
		} catch (InstantiationException e) {
			e.printStackTrace();
			return false;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}