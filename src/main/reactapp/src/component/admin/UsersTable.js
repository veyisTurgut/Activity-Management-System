import React, {Component} from 'react';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';
import axios from "axios";

export default class UsersTable extends Component {
    constructor(props) {
        super(props)
        this.state = {
            UserData: []
        }
    }

    componentDidMount() {
        axios.get('http://localhost:8080/activities/' + this.props.activityTitle + '/users'
            , {
                headers: {
                    'Access-Control-Allow-Origin': '*'
                    , 'Authorization': this.props.jwt
                }
            }
        ).then(value => {
            console.log(value.data)

            this.setState({
                UserData: Object.values(value.data)
            });
        }).catch(reason => {
            console.log(reason.toString())
        });
    }


    render() {
        return (
            <TableContainer component={Paper}>
                <Table aria-label="simple table">
                    <TableHead>
                        <TableRow>
                            <TableCell align="center">Katılımcı İsmi</TableCell>
                            <TableCell align="center">Katılımcı Soyismi</TableCell>
                            <TableCell align="center">Katılımcı E-mail</TableCell>
                            <TableCell align="center">Katılımcı TC Kimlik No</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {
                            this.state.UserData.map((l, index) => {
                                return <TableRow key={index}>
                                    <TableCell align="center">{l.name}</TableCell>
                                    <TableCell align="center">{l.surname}</TableCell>
                                    <TableCell align="center">{l.email}</TableCell>
                                    <TableCell align="center">{l.tcKimlikNo}</TableCell>
                                </TableRow>
                            })
                        }
                    </TableBody>
                </Table>
            </TableContainer>
        );
    }
}
