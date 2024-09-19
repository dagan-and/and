package com.daquv.hub.ibk;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.daquv.hub.App;
import com.daquv.hub.R;
import com.daquv.hub.databinding.FragmentDaquvMapBinding;
import com.daquv.hub.presentation.BaseActivity;
import com.daquv.hub.presentation.conf.IntentConst;
import com.daquv.hub.presentation.util.AppUtil;
import com.daquv.hub.presentation.webview.WebActivity;
import com.daquv.sdk.DaquvConfig;
import com.daquv.sdk.DaquvSDK;
import com.daquv.sdk.data.response.LocationItem;
import com.daquv.sdk.data.response.LocationItemModel;
import com.daquv.sdk.utils.DaquvUtil;
import com.daquv.sdk.utils.Logger;
import com.daquv.sdk.utils.SharedPref;
import com.google.gson.Gson;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.clustering.Cluster;
import com.naver.maps.map.clustering.ClusterMarkerInfo;
import com.naver.maps.map.clustering.ClusterMarkerUpdater;
import com.naver.maps.map.clustering.Clusterer;
import com.naver.maps.map.clustering.Clusterer.ComplexBuilder;
import com.naver.maps.map.clustering.ClusteringKey;
import com.naver.maps.map.clustering.DefaultClusterMarkerUpdater;
import com.naver.maps.map.clustering.DefaultClusterOnClickListener;
import com.naver.maps.map.clustering.DistanceStrategy;
import com.naver.maps.map.clustering.LeafMarkerInfo;
import com.naver.maps.map.clustering.LeafMarkerUpdater;
import com.naver.maps.map.clustering.MarkerInfo;
import com.naver.maps.map.clustering.MarkerManager;
import com.naver.maps.map.clustering.Node;
import com.naver.maps.map.clustering.TagMergeStrategy;
import com.naver.maps.map.overlay.Align;
import com.naver.maps.map.overlay.CircleOverlay;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.MarkerIcons;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MapActivity extends BaseActivity implements OnMapReadyCallback, Overlay.OnClickListener {

    private FragmentDaquvMapBinding mBinding;

    private ExecutorService mExecutor;
    private ArrayList<Marker> markers = new ArrayList<Marker>();
    private ArrayList<LocationItem> locationItem = new ArrayList<LocationItem>();
    private String companyName = null;
    private NaverMap naverMap = null;
    private Marker positionMarker = null;
    private Marker selectMarker = null;
    private ViewGroup dataSheetContainer = null;
    private MapBottomSheetView dataSheetView = null;

    private HashMap<String, Integer> groupCountMap = new HashMap<>();
    private DecimalFormat decimalFormat = new DecimalFormat("#,###");

    //ë§ì»¤ í´ë¦­ì ì´ë ì´ì ë´ê³ 
    //ë¤ë¥¸ ë§ì»¤ í´ë¦­íë©´ ê¸°ì¡´ ì´ë ì´ íë°ê¾¸ ëë©´ì ì´ê¸°í íê³  ì´ë ì´ìì ì§ì°ê¸°

    private MapFragment mapView;
    private Clusterer<ItemKey> clusterer;
    private double currentZoom = 0.0;
    private double lat = DaquvConfig.lat;
    private double lon = DaquvConfig.lon;

    private class ItemKey implements ClusteringKey {
        private final String companyId;
        private final LatLng latLng;

        public ItemKey(String companyId, LatLng latLng) {
            this.companyId = companyId;
            this.latLng = latLng;
        }

        @Override
        public LatLng getPosition() {
            return latLng;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ItemKey itemKey = (ItemKey) o;
            return companyId.equals(itemKey.companyId);
        }

        @Override
        public int hashCode() {
            return companyId.hashCode();
        }
    }

    public class CustomMarkerManager implements MarkerManager {

        @NonNull
        @UiThread
        public final Marker retainMarker(@NonNull MarkerInfo info) {
            return this.createMarker();
        }

        @UiThread
        public final void releaseMarker(@NonNull MarkerInfo info, @NonNull Marker marker) {
            marker.setMap(null);
        }

        @NonNull
        @UiThread
        public Marker createMarker() {
            return new Marker();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = FragmentDaquvMapBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        AppUtil.INSTANCE.setWhiteStatusBareMode(this, true);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.main_status_color));
        mExecutor = Executors.newCachedThreadPool();
        dataSheetContainer = findViewById(R.id.data_sheet);
        mBinding.btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        FragmentManager fm = getSupportFragmentManager();
        mapView = (MapFragment) fm.findFragmentById(R.id.map_container);

        if (mapView == null) {
            mapView = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map_container, mapView).commit();
        }

        checkIntent(getIntent());

        SharedPref.getInstance()
                .put(DaquvConfig.Preference.KEY_NAVI, getString(com.daquv.sdk.R.string.map_tmap));
        SharedPref.getInstance()
                .put(DaquvConfig.Preference.KEY_MULTI_NAVI, getString(com.daquv.sdk.R.string.map_tmap));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            checkIntent(intent);
            if (dataSheetView != null) {
                dataSheetView.updateItem(locationItem);
                dataSheetView.collapseView();
            }
        }
    }


    public void checkIntent(Intent intent) {
        try {
            mExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        String fileName = "companies_99317.json";
                        BufferedReader reader = new BufferedReader(new InputStreamReader(App.Companion.context().getAssets().open(fileName)));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                        reader.close();
                        LocationItemModel locationItemResponse = new Gson().fromJson(sb.toString(), LocationItemModel.class);
                        ArrayList<LocationItem> item = locationItemResponse.getBody().getBody();
                        locationItem = item;
                        Map<ItemKey, LocationItem> map = new HashMap<>();
                        for (LocationItem it : item) {
                            ItemKey itemKey = new ItemKey(it.getCompanyId(), new LatLng(it.getLatitude(), it.getLongitude()));
                            // ê·¸ë£¹ ë§ì»¤ ê°ì map ìì±
                            if ( groupCountMap.get(it.getAddress_group()) == null ) {
                                groupCountMap.put(it.getAddress_group(), 1);
                            } else {
                                groupCountMap.put(it.getAddress_group(), groupCountMap.get(it.getAddress_group()) + 1);
                            }

                            map.put(itemKey, it);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (clusterer != null) {
                                    clusterer.addAll(map);
                                    clusterer.setMap(naverMap);
                                }
                                setDataSheetView();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            mBinding.title.setText("ë´ ê¸°ì¤ ì£¼ë³ê¸°ì");

            mapView.getMapAsync(this);

            if (clusterer != null) {
                clusterer.clear();
            }

            if (markers.size() > 0) {
                for (Marker marker : markers) {
                    marker.setMap(null);
                }
            }
            markers.clear();

            mExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    DaquvSDK.getInstance().getAPI().getTTSBinary("ì£¼ë³ê¸°ìì´ " + locationItem.size() + "ê° ì¡°íëììµëë¤.");
                }
            }, 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(NaverMap p0) {
        naverMap = p0;
        Logger.dev("onMapReady");

        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLogoGravity(Gravity.LEFT | Gravity.TOP);
        uiSettings.setLogoMargin(10, 10, 10, 10);

        CircleOverlay circle = new CircleOverlay();
        LocationOverlay locationOverlay = naverMap.getLocationOverlay();
        locationOverlay.setVisible(true);
        locationOverlay.setIcon(OverlayImage.fromResource(R.drawable.img_map_me));
        locationOverlay.setIconHeight(DaquvUtil.convertDPtoPX(getBaseContext(), 26));
        locationOverlay.setIconWidth(DaquvUtil.convertDPtoPX(getBaseContext(), 26));

        // ì§ëë·° ì í ë¦¬ì¤ë
        naverMap.addOnCameraChangeListener(new NaverMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(int i, boolean b) {
                CameraPosition cameraPosition = naverMap.getCameraPosition();
                currentZoom = cameraPosition.zoom;
                Logger.dev("currentZoom Level = " + currentZoom);
            }
        });

        naverMap.addOnCameraIdleListener(new NaverMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                CameraPosition cameraPosition = naverMap.getCameraPosition();
                currentZoom = cameraPosition.zoom;

                circle.setRadius(1000);     // ë°ê²½ 1km
                circle.setColor(getResources().getColor(R.color.circle_background));    // ë°ê²½ ì ë°°ê²½ ì
                locationOverlay.setPosition(cameraPosition.target);
                circle.setCenter(cameraPosition.target);
                circle.setMap(naverMap);
            }
        });
        clusterer = new ComplexBuilder<ItemKey>()
                .minClusteringZoom(7)
                .maxClusteringZoom(18)
                .maxScreenDistance(200)
                .thresholdStrategy(zoom -> 0.0)
                .tagMergeStrategy(new TagMergeStrategy() {
                    @Nullable
                    @Override
                    public Object mergeTag(@NonNull Cluster cluster) {
                        if(cluster.getMaxZoom() < 14) {
                            return cluster.getChildren().get(0).getTag();
                        } else {
                            return null;
                        }
                    }
                })
                .distanceStrategy(new DistanceStrategy() {
                    @Override
                    public double getDistance(int zoom, Node node1, Node node2) {
                        if (node1.getTag() != null && node1.getTag() instanceof LocationItem &&
                                node2.getTag() != null && node2.getTag() instanceof LocationItem) {
                            LocationItem node1Item = (LocationItem) node1.getTag();
                            LocationItem node2Item = (LocationItem) node2.getTag();

                            if (zoom <= 9) {
                                return node1Item.getAddress_si().equals(node2Item.getAddress_si()) ? -1.0 : 1.0;
                            } else if (zoom <= 11) {
                                return node1Item.getAddress_gu().equals(node2Item.getAddress_gu()) ? -1.0 : 1.0;
                            } else if (zoom < 14) {
                                return node1Item.getAddress_dong().equals(node2Item.getAddress_dong()) ? -1.0 : 1.0;
                            }
                            return 1.0;
                        } else {
                            return 1.0;
                        }
                    }
                })
                .markerManager(new CustomMarkerManager() {
                    @NonNull
                    @Override
                    public Marker createMarker() {
                        Marker marker = super.createMarker();
                        marker.setSubCaptionTextSize(10f);
                        marker.setSubCaptionColor(Color.WHITE);
                        marker.setSubCaptionHaloColor(Color.TRANSPARENT);
                        return marker;
                    }
                })
                .clusterMarkerUpdater(new ClusterMarkerUpdater() {
                    @Override
                    public void updateClusterMarker(@NonNull ClusterMarkerInfo info, @NonNull Marker marker) {
                        int size = info.getSize();
                        if (info.getTag() instanceof LocationItem) {
                            marker.setHideCollidedSymbols(true);
                            marker.setForceShowIcon(true);

                            marker.setOnClickListener(new DefaultClusterOnClickListener(info));
                            marker.setCaptionTextSize(12);
                            marker.setCaptionColor(Color.WHITE);
                            marker.setCaptionHaloColor(Color.TRANSPARENT);
                            marker.setCaptionAligns(Align.Center);
                            marker.setAnchor(DefaultClusterMarkerUpdater.DEFAULT_CLUSTER_ANCHOR);

                            if (currentZoom <= 9) {
                                marker.setWidth(DaquvUtil.convertDPtoPX(getBaseContext(), 70));
                                marker.setHeight(DaquvUtil.convertDPtoPX(getBaseContext(), 70));
                                marker.setIcon(OverlayImage.fromResource(R.drawable.bg_cluster_si));

                                marker.setCaptionText(((LocationItem) info.getTag()).getAddress_si() + "\n" + decimalFormat.format(size));

                            } else if (currentZoom <= 11) {
                                marker.setWidth(DaquvUtil.convertDPtoPX(getBaseContext(), 60));
                                marker.setHeight(DaquvUtil.convertDPtoPX(getBaseContext(), 60));
                                marker.setIcon(OverlayImage.fromResource(R.drawable.bg_cluster_gu));

                                marker.setCaptionText(((LocationItem) info.getTag()).getAddress_gu() + "\n" + decimalFormat.format(size));

                            } else if (currentZoom < 14) {
                                marker.setWidth(DaquvUtil.convertDPtoPX(getBaseContext(), 50));
                                marker.setHeight(DaquvUtil.convertDPtoPX(getBaseContext(), 50));
                                marker.setIcon(OverlayImage.fromResource(R.drawable.bg_cluster_dong));

                                marker.setCaptionTextSize(10);
                                marker.setCaptionText(((LocationItem) info.getTag()).getAddress_dong() + "\n" + decimalFormat.format(size));
                            }
                        }
                    }
                })
                .leafMarkerUpdater(new LeafMarkerUpdater(){
                    @Override
                    public void updateLeafMarker(@NonNull LeafMarkerInfo info, @NonNull Marker marker) {
                        if(info.getTag() != null && info.getTag() instanceof LocationItem) {
                            LocationItem item = (LocationItem) info.getTag();
                            marker.setHideCollidedSymbols(true);
                            marker.setHideCollidedMarkers(true);
//                            marker.setForceShowIcon(true);
                            marker.setCaptionAligns(Align.Bottom);
                            marker.setCaptionHaloColor(Color.WHITE);
                            marker.setCaptionColor(Color.BLACK);
                            marker.setCaptionTextSize(12);
                            marker.setIconTintColor(Color.TRANSPARENT);
                            marker.setOnClickListener(MapActivity.this);

                            marker.setTag(item); // TODO. ì¤ë³µ ë§ì»¤ ì ë³´ë¥¼ ë¦¬ì¤í¸ íìì¼ë¡ Tagë¡ ë±ë¡

                            if(groupCountMap.containsKey(item.getAddress_group()) && groupCountMap.get(item.getAddress_group()) > 1) {
                                marker.setZIndex(1);

                                marker.setWidth(DaquvUtil.convertDPtoPX(getBaseContext(), 24));
                                marker.setHeight(DaquvUtil.convertDPtoPX(getBaseContext(), 24));
                                marker.setCaptionText( groupCountMap.get(item.getAddress_group()) + "ê±´" );
                                marker.setIcon(OverlayImage.fromResource(R.drawable.img_map_building));

                            } else {
                                // marker.setTag(item);
                                marker.setWidth(DaquvUtil.convertDPtoPX(getBaseContext(), 26));
                                marker.setHeight(DaquvUtil.convertDPtoPX(getBaseContext(), 26));
                                marker.setCaptionText(item.getCompanyNm());

                                if (selectMarker != null && selectMarker.getTag() != null && selectMarker.getTag().equals(item.getCompanyId())) {
                                    if (!TextUtils.isEmpty(item.getIbtrYN()) && "Y".equalsIgnoreCase(item.getIbtrYN())) {
                                        marker.setIcon(OverlayImage.fromResource(R.drawable.img_map_trade_y_s));
                                    } else {
                                        marker.setIcon(OverlayImage.fromResource(R.drawable.img_map_trade_n_s));
                                    }
                                    item.setSelected(true);
                                } else {
                                    if (!TextUtils.isEmpty(item.getIbtrYN()) && "Y".equalsIgnoreCase(item.getIbtrYN())) {
                                        marker.setIcon(OverlayImage.fromResource(R.drawable.img_map_trade_y));
                                    } else {
                                        marker.setIcon(OverlayImage.fromResource(R.drawable.img_map_trade_n));
                                    }
                                    item.setSelected(false);
                                }
                            }
                        }
                    }
                })
                .build();
    }

    @Override
    public boolean onClick(Overlay p0) {
        // 1. ì´ì ì ì íë ë§ì»¤ ì´ë¯¸ì§ ìë³µ
        resetMarkers();

        // 2. ë§ì»¤ Select ì´ë¯¸ì§ ë³ê²½
        Marker marker = (Marker) p0;
        LocationItem item = (LocationItem) p0.getTag();
        if(groupCountMap.containsKey(item.getAddress_group()) && groupCountMap.get(item.getAddress_group()) > 1) {
            // ê·¸ë£¹ ë§ì»¤ì¼ ë
            marker.setIcon(OverlayImage.fromResource(R.drawable.img_map_building_s));
        } else {
            if (!TextUtils.isEmpty(item != null ? item.getIbtrYN() : null) && "Y".equalsIgnoreCase(item.getIbtrYN())) {
                marker.setIcon(OverlayImage.fromResource(R.drawable.img_map_trade_y_s));
            } else {
                marker.setIcon(OverlayImage.fromResource(R.drawable.img_map_trade_n_s));
            }
        }
        marker.setIconTintColor(Color.TRANSPARENT);

        // 3. Select Marker ì ë³´ ì ì¥
        selectMarker = marker;

        if(groupCountMap.containsKey(item.getAddress_group()) && groupCountMap.get(item.getAddress_group()) > 1) {
            // TODO. ê·¸ë£¹ ë§ì»¤ì¼ ëë ê·¸ë£¹ ë¦¬ì¤í¸ë¥¼ tagì ì ë¬
            selectMarker.setTag(marker.getTag());
        } else {
            selectMarker.setTag(marker.getTag());
        }
        markers.add(marker);

        // 4. ìí¸ ë·°ì ë³´ì¬ì§ê¸°
        dataSheetView.setSelectItem(item.getCompanyId());
        Logger.dev("lat = " + item.getLatitude() + ", lon = " + item.getLongitude() + ", groupName = " + item.getAddress_group());
        return false;
    }

    private void resetMarkers() {
        for (Marker marker : markers) {
            LocationItem item = (LocationItem) marker.getTag();
            if (groupCountMap.containsKey(item.getAddress_group()) && groupCountMap.get(item.getAddress_group()) > 1) {
                // ê·¸ë£¹ ë§ì»¤ì¼ ë
                marker.setIcon(OverlayImage.fromResource(R.drawable.img_map_building));
            } else {
                if (!TextUtils.isEmpty(item != null ? item.getIbtrYN() : null) && "Y".equalsIgnoreCase(item.getIbtrYN())) {
                    marker.setIcon(OverlayImage.fromResource(R.drawable.img_map_trade_y));
                } else {
                    marker.setIcon(OverlayImage.fromResource(R.drawable.img_map_trade_n));
                }
            }
            marker.setIconTintColor(Color.TRANSPARENT);
        }
        markers.clear();
    }

    private void setDataSheetView() {
        // Handle actions when the keyboard is shown, and other configurations
        dataSheetView = new MapBottomSheetView(this,
                dataSheetContainer,
                locationItem,
                new MapBottomSheetView.OnStateListener() {
                    @Override
                    public void onStateChanged(int newState) {
                        // Do nothing
                    }

                    @Override
                    public void onItemClick(String tag, LocationItem data) {
                        if ("Container".equals(tag)) {
                            // Navigate to the answer screen
                            try {
                                String url = DaquvUtil.getUrl("/webview/bizdaquv/v1/CRM011");

                                Intent webIntent = new Intent();
                                webIntent.setAction(IntentConst.Action.ACTION_WEB_VIEW);
                                webIntent.putExtra(IntentConst.Extras.EXTRA_WEB_VIEW_URL, url);
                                webIntent.putExtra(IntentConst.Extras.EXTRA_WEB_VIEW_DATA, "");
                                webIntent.setClass(MapActivity.this, WebActivity.class);
                                startActivity(webIntent);

                            } catch (NullPointerException e) {
                                Logger.error(e);
                            }
                        } else if ("Navi".equals(tag)) {
                            ArrayList<LocationItem> mark = new ArrayList<>();
                            mark.add(new LocationItem("ë´ ìì¹", lat, lon));
                            mark.add(data);

                            String naviPref = SharedPref.getInstance().getString(DaquvConfig.Preference.KEY_NAVI);
                            if (getString(R.string.map_tmap).equals(naviPref)) {
                                DaquvUtil.runTMap(MapActivity.this, mark);
                            } else if (getString(R.string.map_kakao).equals(naviPref)) {
                                DaquvUtil.runKakaoMap(MapActivity.this, mark);
                            } else if (getString(R.string.map_naver).equals(naviPref)) {
                                DaquvUtil.runNaverMap(MapActivity.this, mark);
                            }
                        }
                    }

                    @Override
                    public void onNaviClick(ArrayList<LocationItem> datas) {
                        ArrayList<LocationItem> mark = new ArrayList<>();
                        mark.add(new LocationItem("ë´ ìì¹", lat, lon));
                        mark.addAll(datas);

                        String multiNaviPref = SharedPref.getInstance().getString(DaquvConfig.Preference.KEY_MULTI_NAVI);
                        if (getString(R.string.map_tmap).equals(multiNaviPref)) {
                            DaquvUtil.runTMap(MapActivity.this, mark);
                        } else if (getString(R.string.map_naver).equals(multiNaviPref)) {
                            DaquvUtil.runNaverMap(MapActivity.this, mark);
                        }
                    }
                });
        dataSheetView.updateItem(locationItem);
        dataSheetView.collapseView();
    }

    @Override
    public void onBackPressed() {
        if (dataSheetView != null && dataSheetView.isStateExpanded()) {
            dataSheetView.collapseView();
            return;
        }
        super.onBackPressed();
        DaquvSDK.getInstance().stopTTS();
        overri