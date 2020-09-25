import React from 'react';
import Button from '@material-ui/core/Button';
import Snackbar from '@material-ui/core/Snackbar';
import MuiAlert from '@material-ui/lab/Alert';
import {makeStyles} from '@material-ui/core/styles';

function Alert(props) {
    return <MuiAlert elevation={6} variant="filled" {...props} />;
}

const useStyles = makeStyles((theme) => ({
    root: {
        width: '100%',
        '& > * + *': {
            marginTop: theme.spacing(2),
        },
    },
}));

export default function CustomizedSnackbars(props) {
    const classes = useStyles();
   
    return (
        <div className={classes.root}>
            <Snackbar open={props.open} autoHideDuration={4000} onClose={props.handleCloseToast}>
                <Alert onClose={props.handleCloseToast} severity={props.messageType.toLowerCase()}>
                    {props.message}
                </Alert>
            </Snackbar>
        </div>
    );
}
