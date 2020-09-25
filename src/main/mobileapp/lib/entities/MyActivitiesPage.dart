import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:http/http.dart' as http;
import 'package:qr_flutter/qr_flutter.dart';
import 'GlobalVars.dart';

import 'Activity.dart';

class MyActivitiesPage extends StatefulWidget {
  @override
  _ApplicationsPageState createState() => _ApplicationsPageState();
}

class _ApplicationsPageState extends State<MyActivitiesPage> {
  List<Activity> _myActivities = List<Activity>();
  String Tc;

  Future<List<Activity>> fetchActivities() async {
    SharedPreferences myPrefs = await SharedPreferences.getInstance();
    Tc = myPrefs.getString('Tc');
    var activeActivities = await http.get("http://10.0.2.2:8080/users/" + Tc + "/activities");

    var myActivities = List<Activity>();
    if (activeActivities.statusCode == 200) {
      var activitiesJson = json.decode(utf8.decode(activeActivities.bodyBytes));
      for (var activityJson in activitiesJson) {
        myActivities.add(Activity.fromJson(activityJson));
      }
    } else {
      print("hata");
    }
    return myActivities;
  }

  @override
  void initState() {
    fetchActivities().then((value) {
      setState(() {
        _myActivities.addAll(value);
      });
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text(
            "Etkinliklerim",
            textAlign: TextAlign.center,
          ),
        ),
        body: ListView.builder(
          itemBuilder: (context, index) {
            return Card(
              child: Padding(
                  padding: const EdgeInsets.only(top: 32.0, bottom: 32.0, left: 16.0, right: 16.0),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: <Widget>[
                      Text(
                        _myActivities[index].title,
                        style: TextStyle(fontSize: 40, fontWeight: FontWeight.bold),
                      ),
                      Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Row(
                            children: [
                              Text(
                                "Tarih aralığı : ",
                                style: TextStyle(color: Colors.black87, fontSize: 15, fontWeight: FontWeight.bold),
                              ),
                              Text(
                                _myActivities[index].startDate + "  /  ",
                                style: TextStyle(fontSize: 15, color: Colors.grey.shade600),
                              ),
                              Text(
                                _myActivities[index].finishDate,
                                style: TextStyle(fontSize: 15, color: Colors.grey.shade600),
                              ),
                            ],
                          ),
                          Row(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              GestureDetector(
                                onTap: () {
                                  _launchURL("https://www.google.com/maps/search/?api=1&query=" +
                                      _myActivities[index].latitude.toString() +
                                      "," +
                                      _myActivities[index].longitude.toString());
                                },
                                child: Column(
                                  children: <Widget>[
                                    Text("Google Haritalar İçin Tıkla",
                                        style: TextStyle(
                                          fontSize: 15,
                                          decoration: TextDecoration.underline,
                                          fontWeight: FontWeight.bold,
                                        )),
                                  ],
                                ),
                              ),
                            ],
                          ),
                          Row(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              GestureDetector(
                                onTap: () {
                                  Navigator.push(
                                    context,
                                    MaterialPageRoute(
                                      builder: (context) => Scaffold(
                                        appBar: AppBar(),
                                        body: Center(
                                          child: QrImage(
                                            data: "Etkinlik ismi : " +
                                                _myActivities[index].title +
                                                "\nEtkinlik tarihi : " +
                                                _myActivities[index].startDate +
                                                " - " +
                                                _myActivities[index].finishDate +
                                                "\nGoogle Haritalar linki : " +
                                                "https://www.google.com/maps/search/?api=1&query=" +
                                                _myActivities[index].latitude.toString() +
                                                "," +
                                                _myActivities[index].longitude.toString() +
                                                "\n\n Katılımcı Tc'si: " +
                                                Tc,
                                            size: 300,
                                          ),
                                        ),
                                      ),
                                    ),
                                  );
                                },
                                child: QrImage(
                                  padding: EdgeInsets.only(top: 8, right: 5),
                                  data: "Etkinlik ismi : " +
                                      _myActivities[index].title +
                                      "Etkinlik tarihi : " +
                                      _myActivities[index].startDate +
                                      " - " +
                                      _myActivities[index].finishDate +
                                      "\nGoogle Haritalar linki : " +
                                      "https://www.google.com/maps/search/?api=1&query=" +
                                      _myActivities[index].latitude.toString() +
                                      "," +
                                      _myActivities[index].longitude.toString() +
                                      "\n\nKatılımcı Tcsi: " +
                                      Tc,
                                  size: 100,
                                ),
                              )
                            ],
                          ),
                        ],
                      )
                    ],
                  )),
            );
          },
          itemCount: _myActivities.length,
        ));
  }

  _launchURL(url) async {
    if (await canLaunch(url)) {
      await launch(url);
    } else {
      throw 'Could not launch $url';
    }
  }
}

class MyActivitiesController extends StatefulWidget {
  MyActivitiesController({Key key}) : super(key: key);

  @override
  _MyActivitiesControllerState createState() => _MyActivitiesControllerState();
}

class _MyActivitiesControllerState extends State<MyActivitiesController> {
  var userTc;
  TextEditingController userTcCont = new TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          backgroundColor: Colors.cyan,
          title: Text('Kayıt Formu'),
        ),
        body: Form(
          child: Padding(
              padding: const EdgeInsets.only(top: 32.0, bottom: 32.0, left: 16.0, right: 16.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  Text(
                    'Lütfen TC Kimlik Numaranızı Giriniz',
                    style: TextStyle(fontWeight: FontWeight.bold, fontSize: 14.0),
                  ),
                  TextFormField(
                    controller: userTcCont,
                    decoration: InputDecoration(hintText: 'Tc Kimlik No'),
                  ),
                  Container(
                    alignment: Alignment.centerRight,
                    child: MaterialButton(
                      onPressed: () {
                        setState(() {
                          userTc = userTcCont.text;
                        });
                        _saveTc(userTc);
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (context) => MyActivitiesPage(),
                          ),
                        );
                      },
                      color: Colors.green,
                      textColor: Colors.white,
                      child: Text('Etkinliklerimi Getir'),
                    ),
                  ),
                ],
              )),
        ));
  }

  _saveTc(Tc) async {
    SharedPreferences myPrefs = await SharedPreferences.getInstance();
    myPrefs.setString('Tc', Tc);
  }
}
