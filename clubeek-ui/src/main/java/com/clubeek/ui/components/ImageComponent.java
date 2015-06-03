/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clubeek.ui.components;

import com.clubeek.ui.Tools;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Image;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * Component for loading images.
 *
 * @author elopin
 */
public class ImageComponent extends VerticalLayout {

    private int imageWidth;
    private int imageHeight;

    private int imageSize;

    private String photoFile;

    private ByteArrayOutputStream photoOutStream;

    private Image image;

    private Upload btLoadImage;

    public ImageComponent() {
        setSpacing(true);
        setMargin(true);

        image = new Image();

        btLoadImage = new Upload();
        btLoadImage.setImmediate(true);
        btLoadImage.setButtonCaption("Načíst");
        btLoadImage.setReceiver(new Receiver() {

            @Override
            public OutputStream receiveUpload(String filename, String mimeType) {
                photoOutStream = new ByteArrayOutputStream(imageSize);
                photoFile = filename;
                return photoOutStream;
            }

        });

        btLoadImage.addSucceededListener(new SucceededListener() {

            @Override
            public void uploadSucceeded(Upload.SucceededEvent event) {
                if (photoOutStream != null) {
                    if (photoOutStream.size() <= imageSize) {
                    Tools.Components.fillImageByPortrait(image, photoOutStream.toByteArray(), photoFile);
                    } else {
                        throw new RuntimeException("Obrázek je příliš velký. Maximální povolená velikost je " + imageSize + "bajtů.");
                    }
                }
            }
        });
        //btLoadImage.setIcon(FontAwesome.UPLOAD);
        addComponent(image);
        addComponent(btLoadImage);
    }

    /**
     * Sets image widht in pixels.
     * @param imageWidth the imageWidth to set
     */
    public void setImageWidth(int imageWidth) {
        image.setWidth(imageWidth, Unit.PIXELS);
    }

    /**
     * Sets image height in pixels.
     * @param imageHeight the imageHeight to set
     */
    public void setImageHeight(int imageHeight) {
        image.setHeight(imageHeight, Unit.PIXELS);
    }

    /**
     * Maximum size of image in bajts.
     * @param imageSize maximum size of image in bajts
     */
    public void setImageSize(int imageSize) {
        this.imageSize = imageSize;
    }
    
    public byte[] getImageByteArray() {
        return photoOutStream.toByteArray();
    }
    
    public void setPhotoFile(String photoFile) {
        this.photoFile = photoFile;
    }
    
    public void setImage(byte[] imageByteArray) {
        Tools.Components.fillImageByPortrait(image, imageByteArray, photoFile);
    }

}
