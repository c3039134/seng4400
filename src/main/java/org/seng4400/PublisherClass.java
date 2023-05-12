package org.seng4400;

import com.google.api.core.ApiFuture;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

// This class uses Google PubSub and a predefined project and topicId to publish messages to the
// Google service.
public class PublisherClass{
    static int messageTime;
    static int primeMax;

    public static void main(String... args) throws Exception {

        // Declare required Google variables
        String projectId = "seng4400-385204";
        String topicId = "seng4400";


        if(args.length ==0){
            messageTime = 1000; //Time delay between publishing messages in milliSeconds
            primeMax = 100; //The upper limit on the random number generator
        }
        else{
            messageTime = Integer.parseInt(args[0]);
            primeMax = Integer.parseInt(args[1]);
        }

        // Create a publisher
        publisher(projectId, topicId);
    }

    public static void publisher(String projectId, String topicId)
            throws IOException, ExecutionException, InterruptedException {
        TopicName topicName = TopicName.of(projectId, topicId);
        Random random = new Random();
        Publisher publisher = null;
        try {
            // Create a publisher instance with default settings bound to the topic
            publisher = Publisher.newBuilder(topicName).build();

            // Publish a message 100 times
            for(int i = 0; i <= 100; i++) {
                // Create message string format
                String message = "{\n\t\"question\":\"" + random.nextInt(primeMax) + "\"\n}";

                // Output to console
                System.out.println(message);

                // Transform for sending to Google service
                ByteString data = ByteString.copyFromUtf8(message);
                PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

                // Once published, returns a server-assigned message id (unique within the topic)
                ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);
                String messageId = messageIdFuture.get();

                // Create the delay between messages
                TimeUnit.MILLISECONDS.sleep(messageTime);
            }
            //System.out.println("Done");
        } finally {
            if (publisher != null) {
                // When finished with the publisher, shutdown to free up resources.
                publisher.shutdown();
                publisher.awaitTermination(3, TimeUnit.MINUTES);
            }
        }
    }
}