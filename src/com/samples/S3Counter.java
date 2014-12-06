package com.samples;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.route53.model.InvalidArgumentException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

public class S3Counter implements ICounter {
	
	private final String S3_BUCKET_NAME = "s3filecounterbucket";
	private final String S3_OBJ_KEY = "counter";
	
	@Override
	public int getCounter(boolean inc) throws FileNotFoundException, IOException {
		int count = getCounter();
		if(inc)
			incrementCounter();
		
		return count;
	}
	
	@Override
	public int getCounter() throws FileNotFoundException, IOException {
		AWSCredentials credentials = getAWSCredentials();
		AmazonS3 s3 = new AmazonS3Client(credentials);
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        s3.setRegion(usWest2);
        return getCount(s3);
	}
	
	
	public int getCount(AmazonS3 s3) {
        S3Object object = s3.getObject(S3_BUCKET_NAME, S3_OBJ_KEY);
        if(object == null) {
        	saveCounter(0);
        	return 0;
        }
        String objectType = object.getObjectMetadata().getContentType();
        if(!objectType.equalsIgnoreCase("text/plain")) {
        	throw new InvalidArgumentException("Unsupported object type: " + objectType);
        }
        
        int counter = Integer.valueOf(object.getObjectContent().toString());
        return counter;
	}
	
	@Override
	public boolean saveCounter(int count) {
		AWSCredentials credentials = getAWSCredentials();
		AmazonS3 s3 = new AmazonS3Client(credentials);
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        s3.setRegion(usWest2);
        return saveCounter(count, s3);
	}
	
	public boolean saveCounter(int count, AmazonS3 s3) {
        PutObjectResult result = s3.putObject(
    			S3_BUCKET_NAME,
    			S3_OBJ_KEY,
    			new ByteArrayInputStream(String.valueOf(count).getBytes()),
    			new ObjectMetadata());

        String etag = result.getETag();
		return etag != null && !etag.equals("");
	}
	
	@Override
	public boolean incrementCounter() {
		AWSCredentials credentials = getAWSCredentials();
		AmazonS3 s3 = new AmazonS3Client(credentials);
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        s3.setRegion(usWest2);
        return increment(s3);
	}
	
	public boolean increment(AmazonS3 s3) {
		int count = getCount(s3);
		return saveCounter(count+1);
	}
	
	protected AWSCredentials getAWSCredentials() {
		String awsCredsPath = "/Users/vsa/.aws/credentials";
		return new ProfileCredentialsProvider(awsCredsPath, "default").getCredentials();
	}
}
