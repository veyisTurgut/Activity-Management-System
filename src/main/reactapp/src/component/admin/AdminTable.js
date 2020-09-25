import React, {Component} from 'react'
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';
import axios from 'axios';
import {Link} from "@material-ui/core";
import AddDialog from "./AddDialog";
import CustomizedSnackbars from "../Toast";
import DeleteDialog from "./DeleteDialog";
import UpdateDialog from "./UpdateDialog";
import Barchart from "./BarChart";
import UsersTable from "./UsersTable";
import Cookie from "js-cookie"

export default class AdminTable extends Component {
    constructor(props) {
        super(props)
        this.state = {
            activityTitle: "",
            jwt: Cookie.get("token"),
            ProductData: [],
            activityNumbers: [],
            isBarChartOpen: false,
            isAddDialogOpen: false,
            isUpdateDialogOpen: false,
            openToast: false,
            isDeleteDialogOpen: false,
            isShowUsersDialogOpen: false,
            toastMessage: '',
            toastMessageType: '',
            activityDialogFields: [
                {id: "title", label: "Etkinlik Adı", type: "text"},
                {id: "startDate", label: "Başlangıç Tarihi", type: "date"},
                {id: "finishDate", label: "Bitiş Tarihi", type: "date"},
                {id: "latitude", label: "Enlem", type: "number"},
                {id: "longitude", label: "Boylam", type: "number"},
                {id: "remainingQuota", label: "Kota", type: "number"}
            ]
        }
    }

    componentDidMount() {
        axios.get('http://localhost:8080/activities/pastIncluded'
            , {
                headers: {
                    'Access-Control-Allow-Origin': '*'
                    , 'Authorization': this.state.jwt
                }
            }
        )
            .then(value => {
                console.log(this.state.jwt)
                this.setState({
                    ProductData: value.data
                });
            })
            .catch(reason => {
                console.log("jwt: " + this.state.jwt)
                console.log("forbidden i guess")
            });

        axios.get('http://localhost:8080/activities/numbers'
            , {
                headers: {
                    'Access-Control-Allow-Origin': '*'
                    , 'Authorization': this.state.jwt
                }
            }
        )
            .then(value => {
                this.setState({
                    activityNumbers: value.data
                });
            })
            .catch(reason => {
                console.log("jwt: " + this.state.jwt)
                console.log("forbidden i guess")
            });
    }


    handleUpdateActivity = (inputData, activityTitle) => {
        this.setState({isUpdateDialogOpen: false})
        axios.put("http://localhost:8080/activities/" + activityTitle, inputData, {
            headers: {
                'Access-Control-Allow-Origin': '*'
                , 'Authorization': this.state.jwt
            }
        })
            .then(value => {
                this.setState({
                    openToast: true,
                    toastMessage: value.data.message,
                    toastMessageType: value.data.messageType
                });
            }).catch(reason => {
            console.log("jwt: " + this.state.jwt)
            console.log("forbidden i guess")
        });
    }

    handleDeleteActivity = (activityTitle) => {
        this.setState({isDeleteDialogOpen: false})
        axios.delete("http://localhost:8080/activities/" + activityTitle, {
            headers: {
                'Access-Control-Allow-Origin': '*'
                , 'Authorization': this.state.jwt
            }
        })
            .then(value => {
                this.setState({
                    openToast: true,
                    toastMessage: value.data.message,
                    toastMessageType: value.data.messageType
                })
            }).catch(reason => {
            console.log("jwt: " + this.state.jwt)
            console.log("forbidden i guess")
        });
    };

    handleAddActivity = (inputData) => {
        this.setState({isAddDialogOpen: false});
        axios.post("http://localhost:8080/activities/", inputData, {
            headers: {
                'Access-Control-Allow-Origin': '*'
                , 'Authorization': this.state.jwt
            }
        })
            .then(value => {
                this.setState({
                    openToast: true,
                    toastMessage: value.data.message,
                    toastMessageType: value.data.messageType
                });
            }).catch(reason => {
            console.log("jwt: " + this.state.jwt)
            console.log("forbidden i guess")
        });
    }

    handleClickBarChartDialog = () => {
        this.state.isBarChartOpen === true ? this.setState({isBarChartOpen: false})
            : this.setState({isBarChartOpen: true});
    };
    handleClickUsersOfActivitiesTable = () => {
        this.state.isShowUsersDialogOpen === true ? this.setState({isShowUsersDialogOpen: false})
            : this.setState({isShowUsersDialogOpen: true});
    };

    render() {
        return (
            <div>
                <button onClick={() => this.setState({isAddDialogOpen: true})}>
                   Yeni Etkinlik Ekle
                </button>
                <button onClick={this.handleClickBarChartDialog}>
                    Sütun Grafiğinde Katılımcı Sayısını Göster
                </button>
                <TableContainer component={Paper} style={{}}>
                    <Table stickyHeader aria-label="sticky table">
                        <TableHead>
                            <TableRow>
                                <TableCell align="center">Etkinlik İsmi</TableCell>
                                <TableCell align="center">Başlangıç Tarihi</TableCell>
                                <TableCell align="center">Bitiş Tarihi</TableCell>
                                <TableCell align="center"><Link/> Etkinlik Yeri</TableCell>
                                <TableCell align="center"> Kalan Kota</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {
                                this.state.ProductData.map((p, index) => {
                                    var link = "https://www.google.com/maps/search/?api=1&query=" + p.latitude + "," + p.longitude;
                                    return <TableRow key={index}>
                                        <TableCell align="center">{p.title}</TableCell>
                                        <TableCell align="center">{p.startDate}</TableCell>
                                        <TableCell align="center">{p.finishDate}</TableCell>
                                        <TableCell align="center">
                                            <a target="_blank" rel="noopener noreferrer" href={link}> Haritalar</a>
                                        </TableCell>
                                        <TableCell align="center">{p.remainingQuota}</TableCell>
                                        <TableCell align="center">
                                            <button onClick={() => {
                                                this.handleClickUsersOfActivitiesTable();
                                                this.setState({activityTitle: p.title})
                                            }}>
                                                Etkinliğin Katılımcılarını Ayrıntılı Göster
                                            </button>
                                        </TableCell>
                                        <TableCell align="center">
                                            <button onClick={() => this.setState({
                                                isUpdateDialogOpen: true,
                                                activityTitle: p.title
                                            })}>
                                                Güncelle
                                            </button>
                                        </TableCell>
                                        <TableCell align="center">
                                            <button onClick={() => this.setState({
                                                isDeleteDialogOpen: true,
                                                activityTitle: p.title
                                            })}>
                                                Sil
                                            </button>
                                        </TableCell>
                                    </TableRow>
                                })
                            }
                        </TableBody>

                        <UpdateDialog
                            activityTitle={this.state.activityTitle}
                            open={this.state.isUpdateDialogOpen}
                            onSubmit={this.handleUpdateActivity}
                            onClose={() => this.setState({isUpdateDialogOpen: false})}
                            fields={this.state.activityDialogFields}
                        />

                        <DeleteDialog
                            activityTitle={this.state.activityTitle}
                            handleCloseDeleteDialog={() => this.setState({isDeleteDialogOpen: false})}
                            handleDeleteActivity={this.handleDeleteActivity}
                            open={this.state.isDeleteDialogOpen}
                        />

                        <AddDialog
                            activityTitle={this.state.activityTitle}
                            open={this.state.isAddDialogOpen}
                            onClose={() => this.setState({isAddDialogOpen: false})}
                            onSubmit={this.handleAddActivity}
                            fields={this.state.activityDialogFields}/>

                        <CustomizedSnackbars open={this.state.openToast}
                                             onClick={() => this.setState({openToast: true})}
                                             message={this.state.toastMessage}
                                             messageType={this.state.toastMessageType}
                                             handleCloseToast={() => this.setState({openToast: false})}/>
                    </Table>
                </TableContainer>
                <br/>
                <br/>
                <br/>
                <Barchart
                    display={this.state.isBarChartOpen}
                    data={this.state.activityNumbers}
                />
                <br/>
                <br/>
                <br/>
                {this.state.isShowUsersDialogOpen &&
                <UsersTable activityTitle={this.state.activityTitle} jwt={this.state.jwt}/>}
            </div>
        );
    }
}