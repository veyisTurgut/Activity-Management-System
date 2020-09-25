//package yte.intern.project.config;
//
//import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.ehcache.EhCacheCacheManager;
//import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@EnableCaching
//public class CachingConfiguration {
//
//    @Bean
//    public EhCacheManagerFactoryBean cacheManagerFactoryBean(){
//        return new EhCacheManagerFactoryBean();
//    }
//
//    @Bean
//    public EhCacheCacheManager ehCacheCacheManager(){
//
//        CacheConfiguration cacheConfig = new CacheConfiguration()
//                .name("activities")
//                .maxEntriesLocalHeap(1000)
//                .memoryStoreEvictionPolicy("LFU");
//    }
//
//}
