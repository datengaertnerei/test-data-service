import React from "react";

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
            var bdayString = new Intl.DateTimeFormat("de-DE", options).format(new Date(bundle.person.birthDate));
            return (
                <div>
                    <p>{bundle.person.givenName} {bundle.person.familyName}</p>
                    <p>{bundle.person.address.streetAddress} {bundle.person.address.houseNumber}</p>
                    <p>{bundle.person.address.postalCode}  {bundle.person.address.addressLocality}</p>
                    <p>{bdayString} - ID: {bundle.person.taxId}</p>
                    <p>{bundle.landline.phoneNumber}</p>
                    <p>{bundle.mobile.phoneNumber}</p>
                    <p>{bundle.person.email}</p>
                    <p>{bundle.creditCard.type} {bundle.creditCard.number}</p>
                    <p>{bundle.bankAccount.iban}</p>
                    <div><img src={imgurl} /></div>
                </div>
            );
        }
    }
}

export default PersonView;