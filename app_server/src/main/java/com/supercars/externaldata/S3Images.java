/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supercars.externaldata;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.supercars.logging.Logger;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

/**
 *
 * @author tombatchelor
 */
public class S3Images {

    static final Regions CLIENT_REGION = Regions.US_WEST_2;
    static final String BUCKET_NAME = "carimages-observeinc";

    public static BufferedImage getImage(String imageName) {
        BufferedImage image = null;
        try {
            S3Object object = getClient().getObject(BUCKET_NAME, imageName);
            ImageInputStream iin = ImageIO.createImageInputStream(object.getObjectContent());
            image = ImageIO.read(iin);
        } catch (IOException | SdkClientException ex) {
            Logger.log(ex);
        }

        return image;
    }

    public static void saveImage(File image, String name) {
        getClient().putObject(BUCKET_NAME, name, image);
    }

    private static AmazonS3 getClient() {
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new EnvironmentVariableCredentialsProvider())
                .withRegion(CLIENT_REGION)
                .build();
    }
}
