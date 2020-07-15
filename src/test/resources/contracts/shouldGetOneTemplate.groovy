import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description 'Get one template'

    request {
        method 'GET'
        url '/template/1001'
    }
    response {
        status 200
        body"""\
{
    "id": "1001",
    "name": "name: 1001"
}
"""
    }
}