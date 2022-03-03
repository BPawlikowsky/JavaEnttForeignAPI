/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_jnitest_Registry */


// Data structures
struct position {
  float x;
  float y;
};

struct velocity {
  float dx;
  float dy;
};


#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     org_jnitest_Registry
 * Method:    init
 * Signature: ()V
 */
JNIEXPORT void JNICALL init(void);

/*
 * Class:     org_jnitest_Registry
 * Method:    create
 * Signature: ()V
 */
JNIEXPORT position JNICALL create(position&);

/*
 * Class:     org_jnitest_Registry
 * Method:    update
 * Signature: ()V
 */
JNIEXPORT int JNICALL update();

#ifdef __cplusplus
}
#endif
