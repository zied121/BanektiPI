package util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public class CloudinaryConfig {
    private static Cloudinary cloudinary;

    public static Cloudinary getInstance() {
        if (cloudinary == null) {
            cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "dsyszjsc2",
                    "api_key", "967221561574594",
                    "api_secret", "IosNjEIg0uEroIu7yXlS08os2bg"));
        }
        return cloudinary;
    }
}

