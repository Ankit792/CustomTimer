package android.app.lock;
/**
 * System private API for communicating with the Lock Service.
 * {@hide}
 */
interface ILockService {
    void setLockState(boolean locked);
    boolean isLocked();
    void showLockMessage(String message);
}