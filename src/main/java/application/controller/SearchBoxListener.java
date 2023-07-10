package application.controller;

/**
 * Đây là giao diện để cài đặt các thao tác
 * tìm kiếm khi sử dụng thanh tìm kiếm trên giao diện
 */

public interface SearchBoxListener {

    /**
     * Định nghĩa phương thức thực thi khi có sự kiện tìm kiếm theo tên
     * @param name tên cần tìm kiếm
     */
    void handleSearchName(String name);

    /**
     * Định nghĩa phương thức thực thi khi có sự kiện tìm kiếm theo id
     */
    void handleSearchId(String id);

    /**
     * Định nghĩa phương thức thực thi khi có thanh tìm kiếm rỗng
     */
    void handleBlank();
}