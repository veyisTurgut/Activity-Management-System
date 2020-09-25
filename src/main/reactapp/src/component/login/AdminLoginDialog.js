import React, {Component} from 'react';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import axios from "axios";
import {Link, BrowserRouter as Router, Route} from "react-router-dom";
import AdminTable from "../admin/AdminTable";
import Cookie from "js-cookie"
import CustomizedSnackbars from "../Toast";


function admin() {
    return <AdminTable/>
}

export default class AdminLoginDialog extends Component {

    state = {
        showDialog: true,
        showLink: false,
        openToast: false,
        toastMessageType: "",
        toastMessage: "",
        adminDialogFields: [
            {id: "username", label: "Kullanıcı Adı", type: "text"},
            {id: "password", label: "Şifre", type: "password"}
        ],
        inputData: {}
    }


    handleInputChange = (event) => {
        event.persist();
        this.setState(prevState => {
            let inputData = {...prevState.inputData};
            inputData[event.target.id] = event.target.value;
            return {inputData};
        });
    }

    onSubmit(inputData) {
        axios.post("http://localhost:8080/login", inputData,
            {
                headers: {
                    'Access-Control-Allow-Origin': '*'
                }
            })
            .then(value => {
                if (value.status === 200) {
                    //to set a cookie
                    Cookie.set("token", "Bearer " + Object.values(value.data)[0]);
                    this.setState({showLink: true});
                } else {
                    this.setState({
                        openToast: true,
                        toastMessage: value.data.message,
                        toastMessageType: value.data.messageType
                    });
                }
            });
    }


    render() {
        return (
            <Router>
                <Dialog open={this.state.showDialog} aria-labelledby="form-dialog-title">
                    <DialogTitle id="form-dialog-title">Kurum Kullanıcısı Girişi</DialogTitle>
                    <DialogContent>
                        <DialogContentText>
                            Kullanıcı bilgilerinizi giriniz.
                        </DialogContentText>
                        {this.state.adminDialogFields.map(field => (
                            <TextField
                                autoFocus
                                variant="filled"
                                margin="dense"
                                id={field.id}
                                label={field.label}
                                type={field.type}
                                onChange={this.handleInputChange}
                                fullWidth
                                required
                            />
                        ))}
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={() => this.onSubmit(this.state.inputData)}
                                color="primary">
                            Giriş Yap
                        </Button>
                        {this.state.showLink &&
                        <Link to={"/admin"} onClick={() => this.setState({showDialog: false})}>
                            Sayfaya gitmek için tıklayın</Link>}
                    </DialogActions>
                    <CustomizedSnackbars open={this.state.openToast}
                                         onClick={() => this.setState({openToast: true})}
                                         handleCloseToast={() => this.setState({openToast: false})}
                                         message={this.state.toastMessage}
                                         messageType={this.state.toastMessageType}/>
                </Dialog>
                <Route path="/admin" component={admin}/>
            </Router>
        );
    }
}