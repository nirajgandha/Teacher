package com.schoolenglishmedium.teacher.model

class Menu {
    var menuId = 0
    var activeIconPath = 0
    var inActiveIconPath = 0
    var menuName: String = ""
    var isSelected = false
    override fun toString(): String {
        return this.javaClass.simpleName +
                " {\"menuId\":\"" + menuId + "\"," +
                " \"activeIconPath:\"" + activeIconPath + "\"," +
                " \"inActiveIconPath:\"" + inActiveIconPath + "\"," +
                " \"menuName:\"" + menuName + "\"," +
                " \"isSelected:\"" + isSelected + "\"}"
    }
}