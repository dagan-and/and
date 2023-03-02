package com.daquv.hub.presentation.main


import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.daquv.hub.App
import com.daquv.hub.domain.usecase.GetTTSUseCase
import com.daquv.hub.presentation.BaseViewModel
import com.daquv.hub.presentation.conf.AppConfig
import com.daquv.hub.presentation.util.Logger
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


class MainViewModel(private val getTTSUseCase: GetTTSUseCase) : BaseViewModel() {

    private val TAG = "vox_mainViewModel"

    // os에 의해 앱의 프로세스가 죽는 등의 상황에서
    // Single 객체를 가로채기 위함
    private val disposable = CompositeDisposable()

    private val delayMillis = 3000L

    fun lifeCycleStart() {

    }

    fun lifeCycleResume() {

    }

    fun lifeCyclePause() {

    }

    fun lifeCycleStop() {

    }

    private var _message = MutableLiveData<String>()
    val message : MutableLiveData<String>
        get() = _message

    @SuppressLint("SimpleDateFormat")
    fun dynamicLM() {
        disposable.add(
            Observable.fromCallable {
                val httpBuilder = OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .callTimeout(15, TimeUnit.SECONDS)
                httpBuilder.addInterceptor(httpLoggingInterceptor())
                val httpClient = httpBuilder.build()
                val request = Request.Builder()

                //URL
                request.url("https://inside.gigagenie.ai:9080/v2/set-alias-info")

                val key = "46740cc0-e532-5e05-9c40-4ddc062a3a3f"
                val dateTime = SimpleDateFormat("yyyyMMddHHmmssSSS").format(Date())
                val signature = generateHashWithHmac256(dateTime + key, "DAQUV")

                //Header
                request.addHeader("x-auth-apikey", key)
                request.addHeader("x-auth-timestamp", dateTime)
                request.addHeader("x-auth-signature", signature)

                //Body
                val formBody = FormBody.Builder()
                formBody.add("uuid","ccb40da5-d23b-5bbf-9f92-40dc95a89e7c")
                formBody.add("aliasInfo","{\n" +
                        "\"IBKZEROTCH\" : [\n" +
                        "   {\"STT_HEADWORD\" : \"과거거래내역조회해줘\",\"DOKUMS\" : \"과거거래내역조회해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"예금계좌 해지거래내역 보여줘 \",\"DOKUMS\" : \"예금계좌 해지거래내역 보여줘 \"},\n" +
                        "{\"STT_HEADWORD\" : \"펀즈계좌 해지거래내역 보여줘 \",\"DOKUMS\" : \"펀즈계좌 해지거래내역 보여줘 \"},\n" +
                        "{\"STT_HEADWORD\" : \"대출계좌 해지거래내역 보여줘 \",\"DOKUMS\" : \"대출계좌 해지거래내역 보여줘 \"},\n" +
                        "{\"STT_HEADWORD\" : \"외화계좌 해지거래내역 보여줘 \",\"DOKUMS\" : \"외화계좌 해지거래내역 보여줘 \"},\n" +
                        "{\"STT_HEADWORD\" : \"카드계좌 해지거래내역 보여줘 \",\"DOKUMS\" : \"카드계좌 해지거래내역 보여줘 \"},\n" +
                        "{\"STT_HEADWORD\" : \"기업자산관리 서비스 가입해줘\",\"DOKUMS\" : \"기업자산관리 서비스 가입해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"기업자산관리 대시보드보여줘\",\"DOKUMS\" : \"기업자산관리 대시보드보여줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"전 은행 계좌조회해줘\",\"DOKUMS\" : \"전 은행 계좌조회해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"기업자산관리 알림 설정\",\"DOKUMS\" : \"기업자산관리 알림 설정\"},\n" +
                        "{\"STT_HEADWORD\" : \"기업자산관리 서비스 해지\",\"DOKUMS\" : \"기업자산관리 서비스 해지\"},\n" +
                        "{\"STT_HEADWORD\" : \"모바일 보고서\",\"DOKUMS\" : \"모바일 보고서\"},\n" +
                        "{\"STT_HEADWORD\" : \"올해 어음관련 수수료내역 조회해줘\",\"DOKUMS\" : \"올해 어음관련 수수료내역 조회해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"빠른조회서비스 신청/해지해줘\",\"DOKUMS\" : \"빠른조회서비스 신청/해지해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"펀드수익률 빠른 조회\",\"DOKUMS\" : \"펀드수익률 빠른 조회\"},\n" +
                        "{\"STT_HEADWORD\" : \"오픈뱅킹 서비스 가입해줘\",\"DOKUMS\" : \"오픈뱅킹 서비스 가입해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"오픈뱅킹 계좌조회\",\"DOKUMS\" : \"오픈뱅킹 계좌조회\"},\n" +
                        "{\"STT_HEADWORD\" : \"오픈뱅킹으로 카드조회\",\"DOKUMS\" : \"오픈뱅킹으로 카드조회\"},\n" +
                        "{\"STT_HEADWORD\" : \"오픈뱅킹으로 계좌이체\",\"DOKUMS\" : \"오픈뱅킹으로 계좌이체\"},\n" +
                        "{\"STT_HEADWORD\" : \"오픈뱅킹 서비스 해지해줘\",\"DOKUMS\" : \"오픈뱅킹 서비스 해지해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"오픈뱅킹 서비스,기업뱅킹으로 등록해줘\",\"DOKUMS\" : \"오픈뱅킹 서비스,기업뱅킹으로 등록해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"오픈뱅킹관련 마케팅 철회해줘\",\"DOKUMS\" : \"오픈뱅킹관련 마케팅 철회해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"계좌통합관리 서비스 가입해줘\",\"DOKUMS\" : \"계좌통합관리 서비스 가입해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"계조자통합조회\",\"DOKUMS\" : \"계조자통합조회\"},\n" +
                        "{\"STT_HEADWORD\" : \"계좌통합관리 서비스 해지해줘\",\"DOKUMS\" : \"계좌통합관리 서비스 해지해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"계좌통합관리,기업뱅킹으로 등록해줘\",\"DOKUMS\" : \"계좌통합관리,기업뱅킹으로 등록해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"만기일 기준으로 활동중인 보관어음 조회해줘\",\"DOKUMS\" : \"만기일 기준으로 활동중인 보관어음 조회해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"정은주에게 10만원 연월차수당으로 이체해줘\",\"DOKUMS\" : \"정은주에게 10만원 연월차수당으로 이체해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"정은주, 쿠콘 글로벌한테 20만원씩 상여금으로 이체해줘\",\"DOKUMS\" : \"정은주, 쿠콘 글로벌한테 20만원씩 상여금으로 이체해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"원금으로 주계좌 출금해줘\",\"DOKUMS\" : \"원금으로 주계좌 출금해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"10억 주식회사 쿠콘으로 이체해줘\",\"DOKUMS\" : \"10억 주식회사 쿠콘으로 이체해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"강민경에게 100만원 박지원에게 150만원 급여이체해줘\",\"DOKUMS\" : \"강민경에게 100만원 박지원에게 150만원 급여이체해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"자동이체 등록내역 조회해줘\",\"DOKUMS\" : \"자동이체 등록내역 조회해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"강민경에게 매월 3일 30만원 자동이체 등록할게\",\"DOKUMS\" : \"강민경에게 매월 3일 30만원 자동이체 등록할게\"},\n" +
                        "{\"STT_HEADWORD\" : \"1개월 내 자동으로 이체한 결과 조회해줘\",\"DOKUMS\" : \"1개월 내 자동으로 이체한 결과 조회해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"이체등록일 6개월 기준으로 예약이체 조회해줘\",\"DOKUMS\" : \"이체등록일 6개월 기준으로 예약이체 조회해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"30만원 강민경에게 1월 20일 3시로 예약이체 등록해줘\",\"DOKUMS\" : \"30만원 강민경에게 1월 20일 3시로 예약이체 등록해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"안심이체 서비스 가입해줘\",\"DOKUMS\" : \"안심이체 서비스 가입해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"안심이체 진행내역 보여줘\",\"DOKUMS\" : \"안심이체 진행내역 보여줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"안심이체 조회해줘\",\"DOKUMS\" : \"안심이체 조회해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"안심이체 고객정보관리\",\"DOKUMS\" : \"안심이체 고객정보관리\"},\n" +
                        "{\"STT_HEADWORD\" : \"안심이체 판매물품관리\",\"DOKUMS\" : \"안심이체 판매물품관리\"},\n" +
                        "{\"STT_HEADWORD\" : \"간편송금 서비스 가입\",\"DOKUMS\" : \"간편송금 서비스 가입\"},\n" +
                        "{\"STT_HEADWORD\" : \"간편송금\",\"DOKUMS\" : \"간편송금\"},\n" +
                        "{\"STT_HEADWORD\" : \"간편송금 내역 조회해줘\",\"DOKUMS\" : \"간편송금 내역 조회해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"PIN 변경해줘\",\"DOKUMS\" : \"PIN 변경해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"간편송금 서비스 해지해줘\",\"DOKUMS\" : \"간편송금 서비스 해지해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"지연이체 서비스가입해줘\",\"DOKUMS\" : \"지연이체 서비스가입해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"지연이체내역조회/취소해줘\",\"DOKUMS\" : \"지연이체내역조회/취소해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"부가세 즉시 결제\",\"DOKUMS\" : \"부가세 즉시 결제\"},\n" +
                        "{\"STT_HEADWORD\" : \"부가세 전용계좌로 이체해줘\",\"DOKUMS\" : \"부가세 전용계좌로 이체해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"1년치 금 매입 내역 조회해줘\",\"DOKUMS\" : \"1년치 금 매입 내역 조회해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"부가세환급내역조회해줘\",\"DOKUMS\" : \"부가세환급내역조회해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"부가세 매입 계좌 전환해줘\",\"DOKUMS\" : \"부가세 매입 계좌 전환해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"거래처 정보관리해줘\",\"DOKUMS\" : \"거래처 정보관리해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"착오송금 변환해줘\",\"DOKUMS\" : \"착오송금 변환해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"개인자사업자카드 인기순으로 조회해줘\",\"DOKUMS\" : \"개인자사업자카드 인기순으로 조회해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"카드발급현황 조회해줘\",\"DOKUMS\" : \"카드발급현황 조회해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"신용카드 등록할게\",\"DOKUMS\" : \"신용카드 등록할게\"},\n" +
                        "{\"STT_HEADWORD\" : \"현금카드 등록할게\",\"DOKUMS\" : \"현금카드 등록할게\"},\n" +
                        "{\"STT_HEADWORD\" : \"명세서 재발송 신청해줘\",\"DOKUMS\" : \"명세서 재발송 신청해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"명세서 표기제외 신청해줘\",\"DOKUMS\" : \"명세서 표기제외 신청해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"1년지 카드 결제완료금액 얼마야?\",\"DOKUMS\" : \"1년지 카드 결제완료금액 얼마야?\"},\n" +
                        "{\"STT_HEADWORD\" : \"카드 전체 미결제금액 조회해줘\",\"DOKUMS\" : \"카드 전체 미결제금액 조회해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"일시불 선결제내역 조회해줘\",\"DOKUMS\" : \"일시불 선결제내역 조회해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"체크카드 한도 조회해줘\",\"DOKUMS\" : \"체크카드 한도 조회해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"카드 비밀번호 변경해줘\",\"DOKUMS\" : \"카드 비밀번호 변경해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"카드 결제정보 변경해줘\",\"DOKUMS\" : \"카드 결제정보 변경해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"카드 이용 OFF해줘\",\"DOKUMS\" : \"카드 이용 OFF해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"해외카드 이용 OFF해줘\",\"DOKUMS\" : \"해외카드 이용 OFF해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"해외 원화결제 OFF해줘\",\"DOKUMS\" : \"해외 원화결제 OFF해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"카드 정지사유 조회해줘\",\"DOKUMS\" : \"카드 정지사유 조회해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"카드 계약서 조회해줘\",\"DOKUMS\" : \"카드 계약서 조회해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"카드해지해줘\",\"DOKUMS\" : \"카드해지해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"카드 실적충족 현황조회\",\"DOKUMS\" : \"카드 실적충족 현황조회\"},\n" +
                        "{\"STT_HEADWORD\" : \"카드 포인트조회\",\"DOKUMS\" : \"카드 포인트조회\"},\n" +
                        "{\"STT_HEADWORD\" : \"카드로 4대보험 자동납부\",\"DOKUMS\" : \"카드로 4대보험 자동납부\"},\n" +
                        "{\"STT_HEADWORD\" : \"카드로 세금납부\",\"DOKUMS\" : \"카드로 세금납부\"},\n" +
                        "{\"STT_HEADWORD\" : \"바로알림 서비스\",\"DOKUMS\" : \"바로알림 서비스\"},\n" +
                        "{\"STT_HEADWORD\" : \"카드 VIP제도 안내해줘\",\"DOKUMS\" : \"카드 VIP제도 안내해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"카드 VIP 등급 조회해줘\",\"DOKUMS\" : \"카드 VIP 등급 조회해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"카드 등급별 우대서비스 조회해줘\",\"DOKUMS\" : \"카드 등급별 우대서비스 조회해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"사업장보안\",\"DOKUMS\" : \"사업장보안\"},\n" +
                        "{\"STT_HEADWORD\" : \"IBK비즈프레소 보여줘\",\"DOKUMS\" : \"IBK비즈프레소 보여줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"해외부가세 환급\",\"DOKUMS\" : \"해외부가세 환급\"},\n" +
                        "{\"STT_HEADWORD\" : \"무료 법률상담 해줘\",\"DOKUMS\" : \"무료 법률상담 해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"IBK 법인카드APP 설치할게\",\"DOKUMS\" : \"IBK 법인카드APP 설치할게\"},\n" +
                        "{\"STT_HEADWORD\" : \"기업대출 고정금리인 상품 추천해줘\",\"DOKUMS\" : \"기업대출 고정금리인 상품 추천해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"비대면대출 상품추천해줘\",\"DOKUMS\" : \"비대면대출 상품추천해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"신청중인 대출 조회해줘\",\"DOKUMS\" : \"신청중인 대출 조회해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"대출계좌조회해줘\",\"DOKUMS\" : \"대출계좌조회해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"대출거래내역조회해줘\",\"DOKUMS\" : \"대출거래내역조회해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"대출금상환할게\",\"DOKUMS\" : \"대출금상환할게\"},\n" +
                        "{\"STT_HEADWORD\" : \"대출 기간연장\",\"DOKUMS\" : \"대출 기간연장\"},\n" +
                        "{\"STT_HEADWORD\" : \"대출 기간연장 약정취소\",\"DOKUMS\" : \"대출 기간연장 약정취소\"},\n" +
                        "{\"STT_HEADWORD\" : \"보증부대출신청할게\",\"DOKUMS\" : \"보증부대출신청할게\"},\n" +
                        "{\"STT_HEADWORD\" : \"보증부대출실행해줘\",\"DOKUMS\" : \"보증부대출실행해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"대출계약서류 이용안내\",\"DOKUMS\" : \"대출계약서류 이용안내\"},\n" +
                        "{\"STT_HEADWORD\" : \"대출계약서류 계약서류 조회\",\"DOKUMS\" : \"대출계약서류 계약서류 조회\"},\n" +
                        "{\"STT_HEADWORD\" : \"대출 청약철회 신청\",\"DOKUMS\" : \"대출 청약철회 신청\"},\n" +
                        "{\"STT_HEADWORD\" : \"대출 청약철회 진행현황 조회\",\"DOKUMS\" : \"대출 청약철회 진행현황 조회\"},\n" +
                        "{\"STT_HEADWORD\" : \"대출 적합성/적정성 고객정보 확인서 제출\",\"DOKUMS\" : \"대출 적합성/적정성 고객정보 확인서 제출\"},\n" +
                        "{\"STT_HEADWORD\" : \"대출 적합성/적정성 고객정보 확인서 작성내역 조회\",\"DOKUMS\" : \"대출 적합성/적정성 고객정보 확인서 작성내역 조회\"},\n" +
                        "{\"STT_HEADWORD\" : \"근저당권 유지확인서\",\"DOKUMS\" : \"근저당권 유지확인서\"},\n" +
                        "{\"STT_HEADWORD\" : \"메디칼네트워크론약정현황 보여줘\",\"DOKUMS\" : \"메디칼네트워크론약정현황 보여줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"메디칼네트워크론대출 신청\",\"DOKUMS\" : \"메디칼네트워크론대출 신청\"},\n" +
                        "{\"STT_HEADWORD\" : \"메디칼네트워크론 상환할게\",\"DOKUMS\" : \"메디칼네트워크론 상환할게\"},\n" +
                        "{\"STT_HEADWORD\" : \"기업회전대출약정 현황 보여줘\",\"DOKUMS\" : \"기업회전대출약정 현황 보여줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"기업회전대출 신청\",\"DOKUMS\" : \"기업회전대출 신청\"},\n" +
                        "{\"STT_HEADWORD\" : \"기업회전대출 상환 현황\",\"DOKUMS\" : \"기업회전대출 상환 현황\"},\n" +
                        "{\"STT_HEADWORD\" : \"프랜차이즈모바일론 대출 신청\",\"DOKUMS\" : \"프랜차이즈모바일론 대출 신청\"},\n" +
                        "{\"STT_HEADWORD\" : \"해외 외화송금\",\"DOKUMS\" : \"해외 외화송금\"},\n" +
                        "{\"STT_HEADWORD\" : \"국내 외화송금\",\"DOKUMS\" : \"국내 외화송금\"},\n" +
                        "{\"STT_HEADWORD\" : \"QR코드\",\"DOKUMS\" : \"QR코드\"},\n" +
                        "{\"STT_HEADWORD\" : \"무역대전 송금해줘\",\"DOKUMS\" : \"무역대전 송금해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"6개월치 최근일기준으로 무역대전송금 내역 조회해줘\",\"DOKUMS\" : \"6개월치 최근일기준으로 무역대전송금 내역 조회해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"외화송금 조건 변경해줘\",\"DOKUMS\" : \"외화송금 조건 변경해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"외화송금 조건변경내역 조회\",\"DOKUMS\" : \"외화송금 조건변경내역 조회\"},\n" +
                        "{\"STT_HEADWORD\" : \"강민경에게 바로 100달러 송금해줘\",\"DOKUMS\" : \"강민경에게 바로 100달러 송금해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"10만엔 강민경에게 환전이체해줘\",\"DOKUMS\" : \"10만엔 강민경에게 환전이체해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"최근기준으로 3개월치 보낸 외화송금내역 보여줘\",\"DOKUMS\" : \"최근기준으로 3개월치 보낸 외화송금내역 보여줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"최근기준으로 3개월치 받은 외화송금내역 보여줘\",\"DOKUMS\" : \"최근기준으로 3개월치 받은 외화송금내역 보여줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"수입신용장조회\",\"DOKUMS\" : \"수입신용장조회\"},\n" +
                        "{\"STT_HEADWORD\" : \"수입신용장관련 수수료조회\",\"DOKUMS\" : \"수입신용장관련 수수료조회\"},\n" +
                        "{\"STT_HEADWORD\" : \"L/G발행내역조회\",\"DOKUMS\" : \"L/G발행내역조회\"},\n" +
                        "{\"STT_HEADWORD\" : \"B/L도착조회\",\"DOKUMS\" : \"B/L도착조회\"},\n" +
                        "{\"STT_HEADWORD\" : \"B/L결제대상조회\",\"DOKUMS\" : \"B/L결제대상조회\"},\n" +
                        "{\"STT_HEADWORD\" : \"B/L결제내역/인수조회\",\"DOKUMS\" : \"B/L결제내역/인수조회\"},\n" +
                        "{\"STT_HEADWORD\" : \"수입수수료납부대상조회\",\"DOKUMS\" : \"수입수수료납부대상조회\"},\n" +
                        "{\"STT_HEADWORD\" : \"수입할인료내역조회\",\"DOKUMS\" : \"수입할인료내역조회\"},\n" +
                        "{\"STT_HEADWORD\" : \"수출환어음명세조회\",\"DOKUMS\" : \"수출환어음명세조회\"},\n" +
                        "{\"STT_HEADWORD\" : \"수출D/P,D/A명세조회\",\"DOKUMS\" : \"수출D/P,D/A명세조회\"},\n" +
                        "{\"STT_HEADWORD\" : \"수출미인수,미입금내역조회\",\"DOKUMS\" : \"수출미인수,미입금내역조회\"},\n" +
                        "{\"STT_HEADWORD\" : \"수출 인수명세조회\",\"DOKUMS\" : \"수출 인수명세조회\"},\n" +
                        "{\"STT_HEADWORD\" : \"수출하자통보명세조회\",\"DOKUMS\" : \"수출하자통보명세조회\"},\n" +
                        "{\"STT_HEADWORD\" : \"수출부도명세조회\",\"DOKUMS\" : \"수출부도명세조회\"},\n" +
                        "{\"STT_HEADWORD\" : \"수출입금조회\",\"DOKUMS\" : \"수출입금조회\"},\n" +
                        "{\"STT_HEADWORD\" : \"수출수수료조회\",\"DOKUMS\" : \"수출수수료조회\"},\n" +
                        "{\"STT_HEADWORD\" : \"오늘기준 달러 환율 변동 추이 보여줘\",\"DOKUMS\" : \"오늘기준 달러 환율 변동 추이 보여줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"환율 SMS서비스 보여줘\",\"DOKUMS\" : \"환율 SMS서비스 보여줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"외국환거래 신고\",\"DOKUMS\" : \"외국환거래 신고\"},\n" +
                        "{\"STT_HEADWORD\" : \"외국환거래약정 보여줘\",\"DOKUMS\" : \"외국환거래약정 보여줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"비대면 약정현황 조회해줘\",\"DOKUMS\" : \"비대면 약정현황 조회해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"휴면예금조회해줘\",\"DOKUMS\" : \"휴면예금조회해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"통장미정리내역 조회해줘\",\"DOKUMS\" : \"통장미정리내역 조회해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"펀드계좌조회\",\"DOKUMS\" : \"펀드계좌조회\"},\n" +
                        "{\"STT_HEADWORD\" : \"펀드거래내역조회\",\"DOKUMS\" : \"펀드거래내역조회\"},\n" +
                        "{\"STT_HEADWORD\" : \"환매내역조회해줘\",\"DOKUMS\" : \"환매내역조회해줘\"},\n" +
                        "{\"STT_HEADWORD\" : \"펀드해지계좌조회해줘\",\"DOKUMS\" : \"펀드해지계좌조회해줘\"}\n" +
                        "  ]\n" +
                        " }")
                formBody.add("preLearning","true")
                formBody.add("domain","DAQUV")
                request.post(formBody.build())

                    return@fromCallable httpClient.newCall(request.build()).execute()
            }.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnError {
                    Logger.error(it.message)
                }
                .subscribe(
                    {
                        Handler(Looper.getMainLooper()).post {
                            _message.value = it.message
                            Toast.makeText(App.context(), "Success::" + it.message, Toast.LENGTH_SHORT).show()
                        }
                    }, {
                        Handler(Looper.getMainLooper()).post {
                            _message.value = it.message
                            Toast.makeText(App.context(), "Fail::" + it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                ))
    }


    private fun generateHashWithHmac256(message: String, key: String) : String {
        try {
            val hashingAlgorithm = "HmacSHA256" //or "HmacSHA1", "HmacSHA512"
            val bytes = hmac(hashingAlgorithm, key.toByteArray(), message.toByteArray())
            val messageDigest = bytesToHex(bytes)
            Log.i(TAG, "message digest: $messageDigest")
            return messageDigest
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    @Throws(NoSuchAlgorithmException::class, InvalidKeyException::class)
    fun hmac(algorithm: String?, key: ByteArray?, message: ByteArray?): ByteArray {
        val mac = Mac.getInstance(algorithm)
        mac.init(SecretKeySpec(key, algorithm))
        return mac.doFinal(message)
    }

    fun bytesToHex(bytes: ByteArray): String {
        val hexArray = "0123456789abcdef".toCharArray()
        val hexChars = CharArray(bytes.size * 2)
        var j = 0
        var v: Int
        while (j < bytes.size) {
            v = bytes[j].toInt() and 0xFF
            hexChars[j * 2] = hexArray[v ushr 4]
            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
            j++
        }
        return String(hexChars)
    }


    fun getUserKey(context: Context): String {
        var deviceId = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            } catch (e : Exception) {

            }
        }
        if (TextUtils.isEmpty(deviceId)) {
            val sharedPrefs = context.getSharedPreferences("PREF_UNIQUE_ID", Context.MODE_PRIVATE);
            var uniqueID = sharedPrefs.getString("PREF_UNIQUE_ID", null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString().replace("-", "");
                val editor = sharedPrefs.edit();
                editor.putString("PREF_UNIQUE_ID", uniqueID);
                editor.apply();
            }
            deviceId = uniqueID
        }
        return deviceId
    }


    /**
     * OkHttp 로그
     */
    private fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.d("DDDDD" , message)
            }

        })
        return interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    override fun onCleared() {
        super.onCleared()
        // 앱이 통신 중에 프로세스가 종료될 경우(앱이 destory됨)
        // 메모리 손실을 최소화 하기 위해 백그라운드 스레드에서 통신 작업을 중단한다.
        disposable.clear()
    }

    open class Event<out T>(private val content: T) {

        var hasBeenHandled = false
            private set

        fun getContentIfNotHandled(): T? {
            return if (hasBeenHandled) {
                null
            } else {
                hasBeenHandled = true
                content
            }
        }

        fun peekContent(): T = content
    }

}