import React, {Component} from 'react';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';

export default class UpdateDialog extends Component {

    state = {
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

    render() {
        return (
            <Dialog open={this.props.open} onClose={this.props.onClose} aria-labelledby="form-dialog-title">
                <DialogContent>
                    <DialogContentText>
                        Etkinliğin güncel bilgilerini giriniz.
                    </DialogContentText>
                    {this.props.fields.map(field => (
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
                    <Button onClick={this.props.onClose} color="secondary">
                        İptal
                    </Button>
                    <Button
                        onClick={() => this.props.onSubmit(this.state.inputData, this.props.activityTitle)}
                        color="primary">
                        Güncelle
                    </Button>
                </DialogActions>
            </Dialog>
        );
    }
}