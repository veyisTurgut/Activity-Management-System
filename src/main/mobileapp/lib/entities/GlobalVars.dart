import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import './../main.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'dart:async';

class GlobalVars extends StatefulWidget {
  GlobalVars({Key key}) : super(key: key);

  @override
  _GlobalVarsState createState() => _GlobalVarsState();
}

class _GlobalVarsState extends State<GlobalVars> {
  var userName;
  var userSurname;
  var userTc;
  var userMail;

  TextEditingController userNameCont = new TextEditingController();
  TextEditingController userSurnameCont = new TextEditingController();
  TextEditingController userTcCont = new TextEditingController();
  TextEditingController userMailCont = new TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          backgroundColor: Colors.green,
          title: Text('Kayıt Formu'),
        ),
        body: Form(
          child: Padding(
              padding: const EdgeInsets.only(top: 32.0, bottom: 32.0, left: 16.0, right: 16.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  Text(
                    'Lütfen Bilgilerinizi Eksiksiz Giriniz',
                    style: TextStyle(fontWeight: FontWeight.bold, fontSize: 14.0),
                  ),
                  TextFormField(
                    controller: userNameCont,
                    decoration: InputDecoration(hintText: 'İsim'),
                  ),
                  TextFormField(
                    controller: userSurnameCont,
                    decoration: InputDecoration(hintText: 'Soyisim'),
                  ),
                  TextFormField(
                    controller: userTcCont,
                    decoration: InputDecoration(hintText: 'Tc Kimlik No'),
                  ),
                  TextFormField(
                    controller: userMailCont,
                    decoration: InputDecoration(hintText: 'Mail adresi'),
                  ),
                  Container(
                    alignment: Alignment.centerRight,
                    child: MaterialButton(
                      onPressed: () {
                        setState(() {
                          userName = userNameCont.text;
                          userSurname = userSurnameCont.text;
                          userTc = userTcCont.text;
                          userMail = userMailCont.text;
                        });
                        _enrollEvent();
                      },
                      color: Colors.orange,
                      textColor: Colors.white,
                      child: Text('Kaydol'),
                    ),
                  ),
                ],
              )),
        ));
  }

  _enrollEvent() async {
    SharedPreferences myPrefs = await SharedPreferences.getInstance();
    final String title = myPrefs.getString('title');

    var response = await http.post("http://10.0.2.2:8080/activities/" + title,
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
        body: jsonEncode(
            <String, String>{'name': userName, 'surname': userSurname, 'email': userMail, 'tcKimlikNo': userTc}));
    if (response.body.substring(response.body.length - 9) == "SUCCESS\"}") {
      Navigator.pop(context);
    }
  }
}
