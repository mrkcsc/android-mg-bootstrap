package com.miguelgaeta.bootstrap.mg_images;

import android.content.Context;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.squareup.okhttp.OkHttpClient;

/**
 * Created by Miguel Gaeta on 6/23/15.
 */
@SuppressWarnings("UnusedDeclaration")
public class MGImagesConfig {

    private static final String IMAGE_PIPELINE_CACHE_DIR = "app_images_cache";
    private static final String IMAGE_PIPELINE_CACHE_DIR_SMALL = "app_images_cache_small";

    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();

    public static final int MAX_DISK_CACHE_SIZE = 40 * ByteConstants.MB;
    public static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 4;

    public static void init(Context context, OkHttpClient client) {

        ImagePipelineConfig.Builder config;

        if (client != null) {

            config = OkHttpImagePipelineConfigFactory.newBuilder(context, client);

        } else {

            config = ImagePipelineConfig.newBuilder(context);
        }

        // Enable down-sampling.
        config.setDownsampleEnabled(true);
        config.setWebpSupportEnabled(true);

        // Setup caches.
        configureCaches(config, context);

        // Initialize fresco.
        Fresco.initialize(context, config.build());
    }

    public static void init(Context context) {

        init(context, null);
    }

    /**
     * Configures disk and memory cache not to exceed common limits
     */
    private static void configureCaches(ImagePipelineConfig.Builder configBuilder, Context context) {

        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(

            // Max total size of elements in the cache
            MAX_MEMORY_CACHE_SIZE,

            // Max entries in the cache
            Integer.MAX_VALUE,

            // Max total size of elements in eviction queue
            MAX_MEMORY_CACHE_SIZE,

            // Max length of eviction queue
            Integer.MAX_VALUE,

            // Max cache entry size
            Integer.MAX_VALUE);

            configBuilder.setBitmapMemoryCacheParamsSupplier(new Supplier<MemoryCacheParams>() {
                @Override
                public MemoryCacheParams get() {
                    return bitmapCacheParams;
                }
            })

            // Set small images cache.
            .setSmallImageDiskCacheConfig(

                DiskCacheConfig.newBuilder(context)
                    .setBaseDirectoryPath(context.getApplicationContext().getCacheDir())
                    .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR_SMALL)
                    .setMaxCacheSize(MAX_DISK_CACHE_SIZE)
                    .build()
            )

            // Set big images cache.
            .setMainDiskCacheConfig(

                DiskCacheConfig.newBuilder(context)
                    .setBaseDirectoryPath(context.getApplicationContext().getCacheDir())
                    .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)
                    .setMaxCacheSize(MAX_DISK_CACHE_SIZE)
                    .build());
    }
}
