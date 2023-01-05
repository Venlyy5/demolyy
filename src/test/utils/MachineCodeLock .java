public class MachineCodeLock {

    private MachineCodeLock(){}

    private static final Cache<String,Lock> cache = CacheBuilder.newBuilder().expireAfterAccess(24, TimeUnit.HOURS).build();

    public static Lock getLock(String key){
        Lock lock = cache.getIfPresent(key);
        if (lock==null) {
            Lock l = new ReentrantLock();
            cache.put(key,l);
            lock = l;
        }
        return lock;
    }
}