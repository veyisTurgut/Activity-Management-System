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
import FormDialog from "./Dialog";
import CustomizedSnackbars from "../Toast";
import qrCodeImage from "../../MyQRCode.png"
import {BrowserRouter as Router, Route, Link as RouteLink} from "react-router-dom";
import AdminLoginDialog from "../login/AdminLoginDialog";


function login() {
    return <AdminLoginDialog/>
}

export default class ActivityTable extends Component {
    constructor(props) {
        super(props)
        this.state = {
            activityTitle: "",
            ProductData: [],
            isQrVisible: false,
            openUserDialog: false,
            openAdminDialog: false,
            openToast: false,
            toastMessage: '',
            toastMessageType: '',
            adminDialogFields: [
                {id: "username", label: "Kullanıcı Adı", type: "text"},
                {id: "password", label: "Şifre", type: "password"}
            ],
            userDialogFields: [
                {id: "name", label: "İsim", type: "text"},
                {id: "surname", label: "Soyisim", type: "text"},
                {id: "email", label: "E-mail adresi", type: "email"},
                {id: "tcKimlikNo", label: "Tc Kimlik No", type: "number"}
            ]
        }
    }

    componentDidMount() {
        axios.get('http://localhost:8080/activities').then(response => {
            this.setState({
                ProductData: response.data
            });
        });
    }

    handleAdminLoginDialog = (inputData) => {
        this.setState({openAdminDialog: false});
        axios.post("http://localhost:8080/login/", inputData)
            .then(value => {
                this.setState({
                    openToast: true,
                    toastMessage: value.data.message,
                    toastMessageType: value.data.messageType
                });
            })
    }
    handleAddUserDialog = (inputData, activityTitle) => {
        this.setState({openUserDialog: false});
        axios.post("http://localhost:8080/activities/" + activityTitle, inputData
            , {
                headers: {
                    'Access-Control-Allow-Origin': '*',
                    "Access-Control-Allow-Methods": "POST"
                }
            }
        ).then(value => {
            if (value.data.messageType === "SUCCESS") {
                this.setState({
                    isQrVisible: true,
                    openToast: true,
                    toastMessage: value.data.message,
                    toastMessageType: value.data.messageType
                });
            } else {
                this.setState({
                    openToast: true,
                    toastMessage: value.data.message,
                    toastMessageType: value.data.messageType
                });
            }
        }).catch(reason => {
            this.setState({
                openToast: true,
                toastMessage: reason.data.message,
                toastMessageType: "ERROR"
            });
        })
    }

    render() {
        return (
            <Router>
                <div>
                    <RouteLink to={"/login"}> Kurum Kullanıcısı Girişi</RouteLink>
                    <TableContainer component={Paper}>
                        <Table stickyHeader aria-label="sticky table">
                            <TableHead>
                                <TableRow>
                                    <TableCell align="center">Etkinlik İsmi</TableCell>
                                    <TableCell align="center">Başlangıç Tarihi</TableCell>
                                    <TableCell align="center">Bitiş Tarihi</TableCell>
                                    <TableCell align="center"><Link/> Etkinlik Yeri</TableCell>
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
                                            <TableCell align="center">
                                                <button onClick={() => this.setState({
                                                    openUserDialog: true,
                                                    activityTitle: p.title
                                                })}>
                                                    Katıl
                                                </button>
                                            </TableCell>
                                            <FormDialog
                                                activityTitle={this.state.activityTitle}
                                                open={this.state.openUserDialog}
                                                onClose={() => this.setState({openUserDialog: false})}
                                                onSubmit={this.handleAddUserDialog}
                                                fields={this.state.userDialogFields}/>
                                        </TableRow>
                                    })
                                }
                            </TableBody>
                            <CustomizedSnackbars open={this.state.openToast}
                                                 onClick={() => this.setState({openToast: true})}
                                                 handleCloseToast={() => this.setState({openToast: false})}
                                                 message={this.state.toastMessage}
                                                 messageType={this.state.toastMessageType}/>
                        </Table>
                    </TableContainer>
                    <br/>
                    <br/>
                    <br/>
                    {this.state.isQrVisible && <img src={qrCodeImage} alt="logo"/>}
                </div>
                <Route path="/login" component={login}/>
            </Router>

        );
    }
}