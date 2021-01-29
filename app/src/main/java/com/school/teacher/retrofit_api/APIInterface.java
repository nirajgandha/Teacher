package com.school.teacher.retrofit_api;

import com.school.teacher.model.*;

import java.util.ArrayList;

import okhttp3.*;
import retrofit2.Call;
import retrofit2.http.*;

public interface APIInterface {

    @FormUrlEncoded
    @POST(ServerConfig.LOGIN_API)
    Call<LoginResponse> loginApi(@Field("phone") String phone,
                                 @Field("device_type") String device_type,
                                 @Field("device_token") String device_token);

    @FormUrlEncoded
    @POST(ServerConfig.OTP_API)
    Call<LoginResponse> verifyOtpApi(@Field("phone") String phone,
                                     @Field("code") String code,
                                     @Field("otp") String otp);

    @FormUrlEncoded
    @POST(ServerConfig.TEACHER_PROFILE_API)
    Call<TeacherProfileResponse> getTeacherProfileApi(@Field("phone") String phone,
                                                      @Field("code") String code);

    @FormUrlEncoded
    @POST(ServerConfig.GET_STUDENT_LIST_API)
    Call<GetStudentListResponse> getStudentListApi(@Field("phone") String phone);

    @FormUrlEncoded
    @POST(ServerConfig.SELECT_STUDENT_API)
    Call<SelectStudentResponse> selectStudentApi(@Field("phone") String phone,
                                                 @Field("student_id") String student_id);

    @FormUrlEncoded
    @POST(ServerConfig.HOME_WORK_LIST_API)
    Call<GetHomeworkResponse> getHomeWorkListApi(@Field("code") String code);

    @FormUrlEncoded
    @POST(ServerConfig.GET_ATTENDANCE_LIST_API)
    Call<AttendanceListResponse> getAttandanceStudentlist(@Field("class_id") String class_id,
                                                      @Field("section_id") String section_id,
                                                      @Field("date") String date);

    @FormUrlEncoded
    @POST(ServerConfig.GET_SYLLABUS_LIST_API)
    Call<GetSyllabusResponse> getSyllabusListApi(@Field("class_id") String class_id,
                                                 @Field("section_id") String section_id);

    @FormUrlEncoded
    @POST(ServerConfig.GET_NOTICE_LIST_API)
    Call<NoticeResponse> getNoticeListApi(@Field("code") String code);

    @FormUrlEncoded
    @POST(ServerConfig.GET_FOOD_MENU_LIST_API)
    Call<FoodMenuResponse> getFoodMenuListApi(@Field("code") String code);

    @FormUrlEncoded
    @POST(ServerConfig.GET_CO_CURRICULUM_ACTIVITY_LIST_API)
    Call<CoCurriculumResponse> getCoActivityListApi(@Field("code") String code);

    @FormUrlEncoded
    @POST(ServerConfig.GET_EVENT_LIST_API)
    Call<CalendarResponse> getCalendarListApi(@Field("start_date_of_month") String start_date_of_month);

    @FormUrlEncoded
    @POST(ServerConfig.GET_EVENT_LIST_API)
    Call<CalendarResponse> getCalendarDayListApi(@Field("selected_date") String selected_date);

    @Multipart
    @POST(ServerConfig.UPLOAD_HOMEWORK_API)
    Call<UploadHomeworkResponse> uploadHomeworkApi(@Part("student_id") RequestBody student_id,
                                                   @Part("homework_id") RequestBody homework_id,
                                                   @Part MultipartBody.Part[] uploadFiles);

    @FormUrlEncoded
    @POST(ServerConfig.GET_GALLERY_API)
    Call<GalleryResponse> getGalleryListApi(@Field("code") String code);

    @FormUrlEncoded
    @POST(ServerConfig.GET_ACTIVITY_API)
    Call<ToDoActivityResponse> getActivityListApi(@Field("code") String code);

    @FormUrlEncoded
    @POST(ServerConfig.GET_TEACHER_LEAVE_API)
    Call<TeacherLeaveResponse> getTeacherLeaveApi(@Field("code") String code);

    @FormUrlEncoded
    @POST(ServerConfig.GET_CLASS_LIST_API)
    Call<ClassListResponse> getClassListApi(@Field("code") String code);

    @FormUrlEncoded
    @POST(ServerConfig.GET_SECTION_LIST_API)
    Call<SectionListResponse> getSectionListApi(@Field("code") String code);

    @FormUrlEncoded
    @POST(ServerConfig.TAKE_ATTENDANCE_API)
    Call<SubmitAttendanceResponse> submitAttendance(@Field("class_id") String class_id,
                                               @Field("section_id") String section_id,
                                               @Field("studentid[]") ArrayList<String> studentid,
                                               @Field("studentattandancetype[]") ArrayList<String> studentattandancetype,
                                               @Field("studentattandanceremark[]") ArrayList<String> studentattandanceremark,
                                               @Field("date") String date);

    @FormUrlEncoded
    @POST(ServerConfig.GET_HOMEWORK_UPDATE_API)
    Call<HomeWorkUpdateResponse> getHomeworkUpdateApi(@Field("homework_id") String class_id,
                                                      @Field("code") String code);

    @FormUrlEncoded
    @POST(ServerConfig.UPDATE_HOMEWORK_API)
    Call<UpdateHomeworkResponse> updateHomeworkStatusApi(@Field("code") String code,
                                                         @Field("homework_id") String class_id,
                                                         @Field("homework_update_id") String homework_update_id,
                                                         @Field("teacher_comment") String teacher_comment,
                                                         @Field("status") String status);

    @FormUrlEncoded
    @POST(ServerConfig.NEW_LEAVE_REQUEST_API)
    Call<NewTeacherLeaveResponse> newLeaveRequestApi(@Field("code") String code,
                                                     @Field("reason") String reason,
                                                     @Field("from_date") String from_date,
                                                     @Field("to_date") String to_date);

    @FormUrlEncoded
    @POST(ServerConfig.EDIT_LEAVE_REQUEST_API)
    Call<NewTeacherLeaveResponse> editLeaveRequestApi(@Field("code") String code,
                                                     @Field("reason") String reason,
                                                     @Field("from_date") String from_date,
                                                     @Field("to_date") String to_date,
                                                     @Field("leave_id") String leave_id);
}
