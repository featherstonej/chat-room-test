function getCookie(name) {
    return document.cookie
        .split('; ')
        .find((row) => row.startsWith(name + '='))
        ?.split('=')[1]
}
export class ApiError extends Error {
    constructor(status, message) {
        super(message)
        this.status = status
    }
}
export async function api(path, { method = 'GET', body } = {}) {
    const headers = { Accept: 'application/json' }
    if (body !== undefined) headers['Content-Type'] = 'application/json'

    const csrf = getCookie('XSRF-TOKEN')
    if (csrf && method !== 'GET') headers['X-XSRF-TOKEN'] = decodeURIComponent(csrf)
    
        const res = await fetch(path, {
        method,
        headers,
        credentials: 'include',
        body: body !== undefined ? JSON.stringify(body) : undefined,
    })
    const data = res.status === 204 ? null : await res.json().catch(() => null)
    if (!res.ok) throw new ApiError(res.status, data?.error ?? 'Something went wrong')
    return data
}