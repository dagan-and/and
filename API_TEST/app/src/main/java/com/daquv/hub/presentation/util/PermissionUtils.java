package com.daquv.hub.presentation.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;

/**
* 앱 접근권한 Utility
**/
public class PermissionUtils {

    /**
     * 앱 권한 요청 및 검사 지원 OS 버전 여부 반환
     * @return 앱 권한 요청 및 검사 지원 OS 버전 여부 (true : 지원 OS 버전 / false : 지원 OS 버전 아님)
     */
    public static boolean isSupportVersion () {
        Logger.info("Support Permission Request OS Ver (SDK INT) :: " + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 거부 상태 접근권한 리스트 반환
     * @param context Context
     * @param permissions 검사 Permission List
     * @return 거부 상태 접근권한 리스트
     */
    public static ArrayList<String> getDeniedPermissionList(Context context, String... permissions) {
        Logger.info("Check Permissions :: " + Arrays.toString(permissions));
        ArrayList<String> deniedPermissionList = new ArrayList<String>();

        // 권한이 허용되어 있는지 검사
        for (String permission : permissions) {
            if (PermissionChecker.checkSelfPermission(context, permission)
                    != PermissionChecker.PERMISSION_GRANTED) {		// 권한 거부

                deniedPermissionList.add(permission);
            }
        }

        return deniedPermissionList;
    }

    /**
     * 권한 체크
     * <br/><br/>
     * - Activity 에서 사용
     * @param act Activity Context
     * @param RequestCode 권한체크 Request Code
     * @param permissions 권한종류
     * @return 권한 요청 필요 여부 (true : 허용되어 있지 않은 상태, 접근권한 요청 / false : 허용되어 있는 상태 또는 권한 요청이 필요하지 않는 OS Ver)
     */
    public static boolean checkDePermission (Activity act, int RequestCode, String... permissions) {
        Logger.info("RequestCode :: " + RequestCode + " / " + "Check Permissions :: " + Arrays.toString(permissions));

        // 접근권한 요청 필요 여부
        boolean requestPermission = false;

        try {
            if (isSupportVersion()) {
                // 접근권한 요청이 필요한 OS Version >> 권한 허용여부 검사 및 요청

                // 접근권한이 거부 상태인 권한 리스트
                ArrayList<String> deniedPermissionList = getDeniedPermissionList(act, permissions);

                if (!deniedPermissionList.isEmpty()) {
                    // 권한요청 필요 >> 권한 요청
                    requestPermission = true;

                }
            }
        } catch (Exception e) {

        }

        return requestPermission;
    }

    /**
     * 권한 체크
     * <br/><br/>
     * - Activity 에서 사용
     * @param act Activity Context
     * @param RequestCode 권한체크 Request Code
     * @param permissions 권한종류
     * @return 권한 요청 필요 여부 (true : 허용되어 있지 않은 상태, 접근권한 요청 / false : 허용되어 있는 상태 또는 권한 요청이 필요하지 않는 OS Ver)
     */
    public static boolean checkPermission (Activity act, int RequestCode, String... permissions) {

        // 접근권한 요청 필요 여부
        boolean requestPermission = false;

        try {
            if (isSupportVersion()) {
                // 접근권한 요청이 필요한 OS Version >> 권한 허용여부 검사 및 요청

                // 접근권한이 거부 상태인 권한 리스트
                ArrayList<String> deniedPermissionList = getDeniedPermissionList(act, permissions);

                if (!deniedPermissionList.isEmpty()) {
                    // 권한요청 필요 >> 권한 요청
                    requestPermission = true;
                    /** 사용자에게 거부되어 있는 권한 허용 요청 Dialog 호출
                     * - 권한 요청 Dialog 에서 사용자가 허용 / 거부 여부는 activity 에 override 한 onRequestPermissionsResult 로 Callback
                     * - activity 에 onRequestPermissionsResult override 필요
                     */
                    ActivityCompat.requestPermissions(act, deniedPermissionList.toArray(new String[deniedPermissionList.size()]), RequestCode);
                }
            }
        } catch (Exception e) {

        }

        return requestPermission;
    }

    /**
     * Fragment 권한 체크
     * <br/><br/>
     * - Fragment 에서 사용
     *
     * @param frag fragment
     * @param RequestCode 권한체크 Request Code
     * @param permissions 권한종류
     * @return 권한 요청 필요 여부
     */
    public static boolean checkPermission (Fragment frag, int RequestCode, String... permissions) {
        Logger.info("RequestCode :: " + RequestCode + " / " + "Check Permissions :: " + Arrays.toString(permissions));

        // 접근권한 요청 필요 여부
        boolean requestPermission = false;

        try {
            if (isSupportVersion()) {
                // 접근권한 요청이 필요한 OS Version >> 권한 허용여부 검사 및 요청

                // 접근권한이 거부 상태인 권한 리스트
                ArrayList<String> deniedPermissionList = getDeniedPermissionList(frag.getContext(), permissions);

                if (!deniedPermissionList.isEmpty()) {
                    // 권한요청 필요 >> 권한 요청
                    requestPermission = true;
                    /**
                     * - Fragment 에서 onRequestPermissionsResult()을 받는 경우에는 Activity 에 막혀 requestCode가 가려지지 않도록 주의해야한다.
                     * Activity 에서 onRequestPermissionsResult()와 onActivityResult()를 오버라이드 하지 않는다면 상관없지만, 만약 한다면, 반드시 처음에 super 메소드를 실행해야 한다.
                     * - NestedFragment 와 DialogFragment 에서는 onRequestPermissionsResult()를 받을 수 없으며, getParentFragment() 또는 getActivity()를 활용하여 권한을 요청해야 한다.
                     */
                    frag.requestPermissions(deniedPermissionList.toArray(new String[deniedPermissionList.size()]), RequestCode);
                }
            }
        } catch (Exception e) {

        }

        return requestPermission;
    }

    /**
     * 권한 검사 결과 반환
     * @param grantResults onRequestPermissionsResult () 를 통해 받은 '권한 허용' 결과 리스트
     * @return 모든 권한이 허용되어 있는지 여부 (true : 모두 허용 / false : 허용되지 않은 권한이 있음)
     */
    public static boolean checkReqPermissionResult (int [] grantResults) {
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }
}
