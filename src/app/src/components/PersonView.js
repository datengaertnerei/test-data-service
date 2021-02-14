import React from "react";
import PersonPanel from "./PersonPanel";
import Typography from "@material-ui/core/Typography";

class PersonView extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            bundle: null,
        };
    }

    componentDidMount() {
        fetch("/api/v1/bundles")
            .then(res => res.json())
            .then(
                (result) => {
                    this.setState({
                        isLoaded: true,
                        bundle: result[0]
                    });
                },
                // Note: it"s important to handle errors here
                // instead of a catch() block so that we don"t swallow
                // exceptions from actual bugs in components.
                (error) => {
                    this.setState({
                        isLoaded: true,
                        error
                    });
                }
            );
    }

    render() {
        const { error, isLoaded, bundle } = this.state;
        if (error) {
            return <div>Error: {error.message}</div>;
        } else if (!isLoaded) {
            return <div>Loading...</div>;
        } else {
            var imgurl = "/api/v1/avatar?gender=" + bundle.person.gender;
            var options = { year: "numeric", month: "2-digit", day: "2-digit", hour24: true };
            return (
                <Typography>
                    <PersonPanel person={bundle.person} />
                    <p>{bundle.landline.phoneNumber}</p>
                    <p>{bundle.mobile.phoneNumber}</p>
                    <p>{bundle.person.email}</p>
                    <p>{bundle.creditCard.type} {bundle.creditCard.number}</p>
                    <p>{bundle.bankAccount.iban}</p>
                    <div><img src={imgurl} alt="avatar" /></div>
                </Typography>
            );
        }
    }
}

export default PersonView;