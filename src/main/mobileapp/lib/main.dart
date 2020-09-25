import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'entities/MyActivitiesPage.dart';
import 'entities/Activity.dart';
import 'package:url_launcher/url_launcher.dart';
import 'entities/GlobalVars.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'dart:async';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.deepPurple,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: HomePage(),
    );
  }
}

class HomePage extends StatefulWidget {
  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  List<Activity> _activities = List<Activity>();
  var title = "";

  Future<List<Activity>> fetchActivities() async {
    var activeActivities = await http.get("http://10.0.2.2:8080/activities");
    var activities = List<Activity>();
    if (activeActivities.statusCode == 200) {
      var activitiesJson = json.decode(utf8.decode(activeActivities.bodyBytes));
      for (var activityJson in activitiesJson) {
        activities.add(Activity.fromJson(activityJson));
      }
    } else {
      print(activeActivities.statusCode);
    }
    return activities;
  }

  @override
  void initState() {
    fetchActivities().then((value) {
      setState(() {
        _activities.addAll(value);
      });
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          actions: [
            IconButton(
              onPressed: () => fetchActivities().then((value) {
                setState(() {
                  _activities.clear();
                  _activities.addAll(value);
                });
              }),
              icon: Icon(Icons.refresh),
            ),
            FlatButton(
              child: Text("Etkinliklerim"),
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (context) => MyActivitiesController(),
                  ),
                );
              },
            )
          ],
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
                        _activities[index].title,
                        style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                      ),
                      Row(
                        children: [
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
                                    _activities[index].startDate + "  /  ",
                                    style: TextStyle(fontSize: 15, color: Colors.grey.shade600),
                                  ),
                                  Text(
                                    _activities[index].finishDate,
                                    style: TextStyle(fontSize: 15, color: Colors.grey.shade600),
                                  ),
                                ],
                              ),
                              Row(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Text(
                                    "Kalan Kota: ",
                                    style: TextStyle(
                                      fontSize: 15,
                                      color: Colors.black87,
                                      fontWeight: FontWeight.bold,
                                    ),
                                    textAlign: TextAlign.left,
                                  ),
                                  Text(
                                    _activities[index].remainingQuota.toString(),
                                    style: TextStyle(
                                        fontSize: 15,
                                        color:
                                            _activities[index].remainingQuota != 0 ? Colors.grey.shade600 : Colors.red),
                                  ),
                                ],
                              ),
                              GestureDetector(
                                onTap: () {
                                  _launchURL("https://www.google.com/maps/search/?api=1&query=" +
                                      _activities[index].latitude.toString() +
                                      "," +
                                      _activities[index].longitude.toString());
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
                          Column(
                            crossAxisAlignment: CrossAxisAlignment.end,
                            children: [
                              GestureDetector(
                                onTap: () {
                                  Navigator.push(
                                    context,
                                    MaterialPageRoute(
                                      builder: (context) => GlobalVars(),
                                    ),
                                  );
                                  //TODO: shared pref
                                  _saveTitle(_activities[index].title);
                                },
                                child: Column(
                                  children: <Widget>[
                                    Text(
                                      _activities[index].remainingQuota == 0 ? "" : "KATIL",
                                      style: TextStyle(
                                        decoration: TextDecoration.underline,
                                        fontSize: 20,
                                      ),
                                    )
                                  ],
                                ),
                              ),
                            ],
                          ),
                        ],
                      )
                    ],
                  )),
            );
          },
          itemCount: _activities.length,
        ));
  }

  _launchURL(url) async {
    if (await canLaunch(url)) {
      await launch(url);
    } else {
      throw 'Could not launch $url';
    }
  }

  _saveTitle(title) async {
    SharedPreferences myPrefs = await SharedPreferences.getInstance();
    myPrefs.setString('title', title);
  }

}
