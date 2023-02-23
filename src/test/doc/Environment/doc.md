.bat

@echo off
cd C:\Users\001400\AppData\Local\Postman\
start Postman.exe
cd /d E:\develop\Q-Dir\
start Q-Dir_x64.exe
cd E:\develop\Snipaste-2.7.1\
start Snipaste.exe
cd E:\develop\JetBrains\IntelliJ IDEA 2020.1\bin\
start idea64.exe
exit

//-------------------------------------------
=text(row()-1, "AK00")

Excel多级菜单
1.选中菜品列数据 Ctrl+t改为超级表(包含标题)，并给表取名为菜品
2.全列A 数据验证 来源:横向选择菜品和饮料
3.全列B 数据验证 来源:=indirect(A1)

一级下拉变更后清空二级下拉内容
Private Sub Worksheet_Change(ByVal Target As Range)
If Target.Column = 1 Then
    Application.EnableEvents = False
        Target.Offset(, 1).ClearContents
    Application.EnableEvents = True
End If
End Sub


Target.Offset(0, 1).ClearContents 此句意思：选定target单元格偏移一列后的单元格。比如你编辑的是k3单元格，则Ｋ4单元格的值被清空，Target.Offset(0, 12).ClearContents，这个相当于偏移12列后的单元格

Private Sub Worksheet_Change(ByVal Target As Range)
Dim Rng As Range
If Target.Row < 2 Then Exit Sub '修改第一行(标题)不往下执行'
    For Each Rng In Target
        If Rng.Column = 1 Then '修改C列'
            Rng.Offset(0, 1).ClearContents '清除D列'
        End If
        If Rng.Column = 2 Then '修改C列'
            Rng.Offset(0, 1).ClearContents '清除D列'
        End If
    Next
End Sub